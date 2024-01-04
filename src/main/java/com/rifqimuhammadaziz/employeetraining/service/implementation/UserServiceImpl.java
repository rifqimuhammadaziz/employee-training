package com.rifqimuhammadaziz.employeetraining.service.implementation;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.rifqimuhammadaziz.employeetraining.exception.*;
import com.rifqimuhammadaziz.employeetraining.model.oauth.Role;
import com.rifqimuhammadaziz.employeetraining.model.oauth.User;
import com.rifqimuhammadaziz.employeetraining.model.request.EmailRegister;
import com.rifqimuhammadaziz.employeetraining.model.request.LoginRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.RegisterRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.ResetPasswordRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.EmailResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.LoginResponse;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.RoleRepository;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.UserRepository;
import com.rifqimuhammadaziz.employeetraining.service.UserService;
import com.rifqimuhammadaziz.employeetraining.service.email.EmailSender;
import com.rifqimuhammadaziz.employeetraining.utility.DateConverter;
import com.rifqimuhammadaziz.employeetraining.utility.EmailTemplate;
import com.rifqimuhammadaziz.employeetraining.utility.SimpleStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository oAuthRoleRepository;
    private final UserRepository oAuthUserRepository;
    private final UserDetailsService oAuth2UserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplateBuilder restTemplateBuilder;
    private final EmailTemplate emailTemplate;
    private final EmailSender emailSender;
    private final DateConverter dateConverter;

    @Value("1200")//set minute expired token
    private int expiredToken;

    @Value("${BASE_URL}")
    private String baseUrl;


    @Override
    public User register(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            log.error("Passwords must be the same");
            throw new NotMatchPasswordException("Passwords must be the same");
        }

        User user = oAuthUserRepository.checkExistingEmail(registerRequest.getEmail());

        if (user != null) {
            log.error("Username already exists");
            throw new UserAlreadyExistsException("Username already exists");
        } else {
            return registerManual(registerRequest);
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            User userChecked = oAuthUserRepository.findOneByUsername(loginRequest.getEmail());
            if ((userChecked != null) && (passwordEncoder.matches(loginRequest.getPassword(), userChecked.getPassword())) && (!userChecked.isEnabled())) {
                log.warn("Username is not enabled yet");
                throw new UserNotActiveException("This account have not enabled, please complete the registration!");
            }
            if (userChecked == null) {
                log.warn("User not found");
                throw new UserNotFoundException("User Not Found");
            }
            if (!(passwordEncoder.matches(loginRequest.getPassword(), userChecked.getPassword()))) {
                log.warn("Wrong password");
                throw new NotMatchPasswordException("Wrong password");
            }

            ResponseEntity<LoginResponse> response = getToken(loginRequest);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                return response.getBody();
            } else {
                log.warn("User with {} not found", loginRequest.getEmail());
                throw new UserNotFoundException(String.format("User %s with not found", loginRequest.getEmail()));
            }
        } catch (HttpStatusCodeException httpStatusCodeException) {
            log.error("{}", getClass().getSimpleName(), httpStatusCodeException);
            if (httpStatusCodeException.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new InvalidLoginException("Invalid login request");
            }
            throw new UserNotFoundException("Error");
        } catch (Exception exception) {
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @Override
    public String sendEmailRegister(EmailRegister emailRegister) {
        User foundUser = oAuthUserRepository.findOneByUsername(emailRegister.getEmail());
        if (foundUser == null) throw new UserNotFoundException("Email not found");

        String template;
        if (StringUtils.isEmpty(foundUser.getOtp())) {
            String otp = generateAndSaveOtp(foundUser);
            template = setTemplateRegister(foundUser, emailTemplate.getRegisterTemplate(), String.format("http://localhost:8080/web/user-register/index/%s", otp));
        } else {
            template = setTemplateRegister(foundUser, emailTemplate.getOTPTemplate(), foundUser.getOtp());
        }
        emailSender.sendAsync(foundUser.getUsername(), "Register", template);
        return "Thanks, please check your email for activation";
    }

    @Override
    public String validateToken(String token) {
        User user = oAuthUserRepository.findOneByOTP(token);
        if (user == null) throw new NoSuchElementException("Token is not valid");
        if (user.isEnabled()) return "Your account has been active, please login!";

        String today = dateConverter.convertDateToString(new Date());
        String dateToken = dateConverter.convertDateToString(user.getOtpExpiredDate());
        if (Long.parseLong(today) > Long.parseLong(dateToken)) {
            throw new TokenExpiredException("Your token is expired, please get token again");
        }

        // enable user
        user.setEnabled(true);
        oAuthUserRepository.save(user);

        return "Your account is successfully activated, please login!";
    }

    @Override
    public User findUserByOTP(String token) {
        return oAuthUserRepository.findOneByOTP(token);
    }

    @Override
    public void saveUser(User user) {
        oAuthUserRepository.save(user);
    }

    @Override
    public String sendEmailReset(EmailRegister emailRegister) {
        User foundUser = oAuthUserRepository.findOneByUsername(emailRegister.getEmail());
        if (foundUser == null) throw new UserNotFoundException("Email not found");

        String template;
        if (StringUtils.isEmpty(foundUser.getOtp())) {
            String otp = generateAndSaveOtp(foundUser);
            template = setTemplateReset(foundUser, emailTemplate.getResetPassword(), otp);
            oAuthUserRepository.save(foundUser);
        } else {
            template = setTemplateReset(foundUser, emailTemplate.getResetPassword(), foundUser.getOtp());
        }
        emailSender.sendAsync(foundUser.getUsername(), "Reset Password", template);
        return "Thanks, please open your email to reset your password";
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String message;
        User user = oAuthUserRepository.findOneByOTP(resetPasswordRequest.getOtp());
        if (user == null) throw new NoSuchElementException("Token is not valid");
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            log.error("Passwords must be the same");
            throw new NotMatchPasswordException("Passwords must be the same");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        try {
            oAuthUserRepository.save(user);
            message = "success";
        } catch (Exception exception) {
            log.error("Failed to save user", exception);
            throw new ServerException("Failed to save user");
        }

        return message;
    }

    @Override
    @SuppressWarnings("deprecation")
    public EmailResponse<?> repairGoogleSigninAction(MultiValueMap<String, String> params) throws IOException {
        Map<String, String> map = params.toSingleValueMap();
        String accessToken = map.get("accessToken");
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        log.info("Access token: {}", accessToken);
        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName("Oauth2").build();

        Userinfoplus profile;
        try {
            profile = oauth2.userinfo().get().execute();
        } catch (GoogleJsonResponseException e) {
            throw new ServerException(e.getDetails().getMessage());
        }

        profile.toPrettyString();
        User user = oAuthUserRepository.findOneByUsername(profile.getEmail());
        if (user != null) {
            if (!user.isEnabled()) {
                // send to email register
                sendEmailRegister(new EmailRegister(user.getUsername()));
                return new EmailResponse<>(
                        200,
                        user,
                        "register",
                        "success"
                );
            }
            for (Map.Entry<String, String> req : map.entrySet()) {
                log.info("key: " + req.getKey());
                log.info("value: " + req.getValue());
            }

            LoginRequest loginRequest = new LoginRequest(profile.getEmail(), profile.getId());
            String oldPassword = user.getPassword();
            if (!passwordEncoder.matches(profile.getId(), oldPassword)) {
                user.setPassword(passwordEncoder.encode(profile.getId().replaceAll("\\s+", "")));
                oAuthUserRepository.save(user);
            }

            ResponseEntity<LoginResponse> response = getToken(loginRequest);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                // update to old password
                user.setPassword(oldPassword);
                oAuthUserRepository.save(user);
                return new EmailResponse<>(
                        200,
                        response.getBody(),
                        "login",
                        "sukses"
                );
            }
        } else {
            RegisterRequest registerRequest = RegisterRequest.builder()
                    .email(profile.getEmail())
                    .fullName(profile.getName())
                    .password(profile.getId())
                    .confirmPassword(profile.getId())
                    .build();

            User registeredUser = registerManual(registerRequest);
            sendEmailRegister(new EmailRegister(profile.getEmail()));
            return new EmailResponse<>(
                    200,
                    registeredUser,
                    "register",
                    "sukses"
            );
        }
        return new EmailResponse<>();    }

    private User registerManual(RegisterRequest registerRequest) {
        User user = new User();
        List<String> rolesName = List.of("ROLE_USER");
        user.setUsername(registerRequest.getEmail().toLowerCase());
        user.setFullname(registerRequest.getFullName());
        user.setEnabled(false);

        String password = passwordEncoder.encode(registerRequest.getPassword().replaceAll("\\s+", ""));
        List<Role> roles = oAuthRoleRepository.findByNameIn(rolesName);

        user.setRoles(roles);
        user.setPassword(password);
        User savedUser = oAuthUserRepository.save(user);
        log.info("Successfully saved user");
        return savedUser;
    }

    // call oauth api to get token
    private ResponseEntity<LoginResponse> getToken(LoginRequest loginRequest) {
        String oauthUrl = String.format("%s/oauth/token", baseUrl);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(oauthUrl)
                .queryParam("username", loginRequest.getEmail())
                .queryParam("password", loginRequest.getPassword())
                .queryParam("grant_type", "password")
                .queryParam("client_id", "my-client-web")
                .queryParam("client_secret", "password");

        return restTemplateBuilder.build().exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    // generate otp
    private String generateAndSaveOtp(User user) {
        User searchedUser;
        String otp;
        do {
            otp = SimpleStringUtils.randomString(6, true);
            searchedUser = oAuthUserRepository.findOneByOTP(otp);
        } while (searchedUser != null);

        Date dateNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        calendar.add(Calendar.MINUTE, expiredToken);
        Date expirationDate = calendar.getTime();

        user.setOtp(otp);
        user.setOtpExpiredDate(expirationDate);
        oAuthUserRepository.save(user);

        return otp;
    }

    // set template to register
    private String setTemplateRegister(User foundUser, String template, String otp) {
        template = template.replaceAll("\\{\\{USERNAME}}",
                (foundUser.getFullname() == null ? foundUser.getUsername() : foundUser.getFullname()));
        template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", otp);
        template = template.replaceAll("\\{\\{HOSTMAIL}}", "test@test.com");
        return template;
    }

    // set template to reset password
    private String setTemplateReset(User foundUser, String template, String otp) {
        template = template.replaceAll("\\{\\{PASS_TOKEN}}",
                otp);
        template = template.replaceAll("\\{\\{USERNAME}}",
                (foundUser.getUsername() == null ? "UserName" : String.format("%s", foundUser.getUsername())));
        return template;
    }

}



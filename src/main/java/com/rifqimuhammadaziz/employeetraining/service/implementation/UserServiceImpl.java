package com.rifqimuhammadaziz.employeetraining.service.implementation;

import com.rifqimuhammadaziz.employeetraining.config.AppConfig;
import com.rifqimuhammadaziz.employeetraining.model.dao.LoginModel;
import com.rifqimuhammadaziz.employeetraining.model.dao.RegisterModel;
import com.rifqimuhammadaziz.employeetraining.model.oauth.Role;
import com.rifqimuhammadaziz.employeetraining.model.oauth.User;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.RoleRepository;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.UserRepository;
import com.rifqimuhammadaziz.employeetraining.service.UserService;
import com.rifqimuhammadaziz.employeetraining.service.oauth.Oauth2UserDetailsService;
import com.rifqimuhammadaziz.employeetraining.utility.TemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    AppConfig config = new AppConfig();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TemplateResponse templateResponse;

    @Value("${BASEURL}")
    private String baseUrl;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;


    @Override
    public Map register(RegisterModel registerModel) {
        Map map = new HashMap();

        try {

            if(userRepository.checkExistingEmail(registerModel.getEmail()) != null) {
                log.error("Email Already Exists");
                return templateResponse.Error("Email Already Exists","400");
            }if (!templateResponse.isValidEmail(registerModel.getEmail())) {
                log.error("Email Tidak Valid");
                return templateResponse.Error("Email Tidak Valid","400");
            }
            if (!registerModel.getPassword().equals(registerModel.getConfirmPassword())) {
                log.error("Passwords must be the same");
                return templateResponse.Error("Passwords must be the same");
            }
            if(registerModel.getPassword().equals("") || registerModel.getPassword().equals(null)) {
                log.error("password cannot be empty");
                return templateResponse.Error("password cannot be empty");
            }
            if(registerModel.getConfirmPassword().equals("") || registerModel.getConfirmPassword().equals(null)) {
                log.error("Confirm password cannot be empty");
                return templateResponse.Error("Confirm password cannot be empty");
            }
//            String[] roleNames = {"ROLE_USER", "ROLE_READ", "ROLE_WRITE"}; // admin
            String[] roleNames = {"ROLE_USER"}; // user
            User user = new User();
            user.setUsername(registerModel.getEmail().toLowerCase());
            user.setFullname(registerModel.getFullName());

            //step 1 :
            user.setEnabled(false); // matikan user

            String password = passwordEncoder.encode(registerModel.getPassword().replaceAll("\\s+", ""));
            List<Role> r = roleRepository.findByNameIn(roleNames);

            user.setRoles(r);
            user.setPassword(password);
            User obj = userRepository.save(user);
            log.info("Register Success");
            return templateResponse.Success(obj);
        } catch (Exception e) {
            logger.error("Eror registerManual :", e);
            return templateResponse.Error("Error : " + e);
        }
    }

    @Override
    public Map login(LoginModel loginModel) {

        try {
            Map<String, Object> map = new HashMap<>();
            User checkUser = userRepository.findOneByUsername(loginModel.getEmail());

            if (!templateResponse.isValidEmail(loginModel.getEmail())) {
                log.error("Email Tidak Valid");
                return templateResponse.Error("Email Tidak Valid");
            }
            if(checkUser == null) {
                log.error("Email Not Found");
                return templateResponse.Error("Not Found");
            }
            if(loginModel.getPassword().equals("") || loginModel.getPassword().equals(null)) {
                log.error("password cannot be empty");
                return templateResponse.Error("password cannot be empty");
            }

//            if (checkUser.isBlocked()) {
//                return response.Error("Your account is blocked, please contact Admin!");
//            }

            if ((checkUser != null) && (passwordEncoder.matches(loginModel.getPassword(), checkUser.getPassword()))) {
                if (!checkUser.isEnabled()) {
                    map.put("is_enabled", checkUser.isEnabled());
                    return templateResponse.Error(map);
                }
            }
            if (!(passwordEncoder.matches(loginModel.getPassword(), checkUser.getPassword()))) {
                return templateResponse.Error("Wrong Password");
            }
            String url = baseUrl + "/oauth/token?username=" + loginModel.getEmail() +
                    "&password=" + loginModel.getPassword() +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                    ParameterizedTypeReference<Map>() {
                    });


            if (response.getStatusCode() == HttpStatus.OK) {
                User user = userRepository.findOneByUsername(loginModel.getEmail());
                List<String> roles = new ArrayList<>();

                for (Role role : user.getRoles()) {
                    roles.add(role.getName());
                }
                //save token
//                checkUser.setAccessToken(response.getBody().get("access_token").toString());
//                checkUser.setRefreshToken(response.getBody().get("refresh_token").toString());
//                userRepository.save(checkUser);

                userRepository.save(user);


                map.put("access_token", response.getBody().get("access_token"));
                map.put("token_type", response.getBody().get("token_type"));
                map.put("refresh_token", response.getBody().get("refresh_token"));
                map.put("expires_in", response.getBody().get("expires_in"));
                map.put("scope", response.getBody().get("scope"));
                map.put("jti", response.getBody().get("jti"));
                map.put("user", user);


                System.out.println(baseUrl);
                return templateResponse.Success(map);
            } else {
                System.out.println(baseUrl);
                return null;
            }

        } catch (
                HttpStatusCodeException e) {
            e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return templateResponse.Error("invalid login");
            }
            return templateResponse.Error(e);
        } catch (
                Exception e) {
            e.printStackTrace();

            return templateResponse.Error(e);
        }
    }
}



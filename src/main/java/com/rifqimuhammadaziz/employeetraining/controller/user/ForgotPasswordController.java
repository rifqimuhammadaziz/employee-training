package com.rifqimuhammadaziz.employeetraining.controller.user;

import com.rifqimuhammadaziz.employeetraining.config.AppConfig;
import com.rifqimuhammadaziz.employeetraining.model.dao.ForgotPasswordModel;
import com.rifqimuhammadaziz.employeetraining.model.oauth.User;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.UserRepository;
import com.rifqimuhammadaziz.employeetraining.service.UserService;
import com.rifqimuhammadaziz.employeetraining.service.email.EmailSender;
import com.rifqimuhammadaziz.employeetraining.utility.EmailTemplate;
import com.rifqimuhammadaziz.employeetraining.utility.SimpleStringUtils;
import com.rifqimuhammadaziz.employeetraining.utility.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/forgot-password/")
public class ForgotPasswordController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    EmailSender emailSender;

    @Autowired
    EmailTemplate emailTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TemplateResponse templateResponse;

//    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
//    private int expiredToken;

    @Value("1200")//set minute expired token
    private int expiredToken;

    AppConfig config = new AppConfig();


    @PostMapping("/request")
    public Map sendPasswordOTP(@RequestBody ForgotPasswordModel forgotPasswordModel) {

        if (forgotPasswordModel.getEmail() == null) {
            return templateResponse.Error("No email provided");
        }
        User found = userRepository.findOneByUsername(forgotPasswordModel.getEmail());
        if (found == null) {
            return templateResponse.Error("Email not found","404"); //throw new BadRequest("Email not found");
        }
        String template = emailTemplate.getResetPassword();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", otp);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? "" + "@UserName" : "@" + found.getUsername()));

            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? "" + "@UserName" : "@" + found.getUsername()));
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Test - Forget Password", template);
        return templateResponse.Success("Success");
    }


    @PostMapping("/check-token")
    public Map checkTokenValid(@RequestBody ForgotPasswordModel model) {
        if (model.getOtp() == null) {
            return templateResponse.Error("Token " + config.isRequired);
        }
        User user = userRepository.findOneByOTP(model.getOtp());
        if (user == null) {
            return templateResponse.Error("Token not valid");
        }
        return templateResponse.Success("Success");
    }

    @PostMapping("/reset")
    public Map<String, String> resetPassword(@RequestBody ForgotPasswordModel forgotPasswordModel) {

        User user = userRepository.findOneByOTP(forgotPasswordModel.getOtp());
        String success;
        if (user == null) {
            return templateResponse.Error("Token not valid","404");
        }
        if (forgotPasswordModel.getOtp() == null) {
            return templateResponse.Error("Token " + config.isRequired,"404");
        }
        if (forgotPasswordModel.getNewPassword() == null) {
            return templateResponse.Error("New Password " + config.isRequired,"404");
        }
        if (forgotPasswordModel.getConfirmNewPassword() == null) {
            return templateResponse.Error("Confirm New Password " + config.isRequired,"404");
        }
        if (!forgotPasswordModel.getNewPassword().equals(forgotPasswordModel.getConfirmNewPassword())) {
            return templateResponse.Error("Passwords must be the same","400");
        }
        if(forgotPasswordModel.getNewPassword().equals("") || forgotPasswordModel.getNewPassword().equals(null)) {
            return templateResponse.Error("password cannot be empty");
        }
        if(forgotPasswordModel.getConfirmNewPassword().equals("") || forgotPasswordModel.getConfirmNewPassword().equals(null)) {
            return templateResponse.Error("Confirm password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(forgotPasswordModel.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        try {
            userRepository.save(user);
            success = "success";
        } catch (Exception e) {
            return templateResponse.Error("Gagal simpan user");
        }
        return templateResponse.Success(success);
    }


}


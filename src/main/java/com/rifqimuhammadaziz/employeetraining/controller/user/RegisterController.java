package com.rifqimuhammadaziz.employeetraining.controller.user;

import com.rifqimuhammadaziz.employeetraining.config.AppConfig;
import com.rifqimuhammadaziz.employeetraining.model.dao.RegisterModel;
import com.rifqimuhammadaziz.employeetraining.model.oauth.User;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.UserRepository;
import com.rifqimuhammadaziz.employeetraining.service.UserService;
import com.rifqimuhammadaziz.employeetraining.service.email.EmailSender;
import com.rifqimuhammadaziz.employeetraining.service.implementation.UserServiceImpl;
import com.rifqimuhammadaziz.employeetraining.utility.EmailTemplate;
import com.rifqimuhammadaziz.employeetraining.utility.SimpleStringUtils;
import com.rifqimuhammadaziz.employeetraining.utility.TemplateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user-register/")
public class RegisterController {
    @Autowired
    public EmailTemplate emailTemplate;

    //    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
//    private int expiredToken;

    @Value("1200")//set minute expired token
    private int expiredToken;

    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    public EmailSender emailSender;
    @Autowired
    private UserRepository userRepository;

    AppConfig config = new AppConfig();

    @Autowired
    public UserService serviceReq;

    @Autowired
    public TemplateResponse templateResponse;
    @PostMapping("/register")
    public ResponseEntity<Map> saveRegisterManual(@Valid @RequestBody RegisterModel registerModel) throws RuntimeException {

        Map saveNewUser = serviceReq.register(registerModel);
        Map sendOTP = sendEmailegister(registerModel);
        return new ResponseEntity<Map>(saveNewUser, HttpStatus.OK);
    }



    // Step 2: sendp OTP berupa URL: guna updeta enable agar bisa login:
    @PostMapping("/send-otp")//send OTP
    public Map sendEmailegister(@RequestBody RegisterModel registerModel) {
        String message = "Thanks, please check your email for activation.";

        if (registerModel.getEmail() == null) return templateResponse.Error("No email provided");
        User found = userRepository.findOneByUsername(registerModel.getEmail());
        if (found == null) return templateResponse.Error("Email not found","404"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getRegisterTemplate();
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
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? found.getUsername() : found.getUsername()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}",  "http://localhost:9090/api/user-register/register-confirm-otp/" + otp);
            template = template.replaceAll("\\{\\{HOSTMAIL}}",  "test@test.test");
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? found.getUsername() : found.getUsername()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}",  "http://localhost:9090/api/user-register/register-confirm-otp/" + found.getOtp());
            template = template.replaceAll("\\{\\{HOSTMAIL}}",  "test@test.test");
        }
        emailSender.sendAsync(found.getUsername(), "Register", template);
        log.info(template);
        return templateResponse.Success(message);
    }



    @GetMapping("/register-confirm-otp/{token}")
    public ResponseEntity<Map> saveRegisterManual(@PathVariable(value = "token") String tokenOtp) throws RuntimeException {


        User user = userRepository.findOneByOTP(tokenOtp);
        if (null == user) {
            return new ResponseEntity<Map>(templateResponse.Error("OTP tidak ditemukan"), HttpStatus.OK);
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if(Long.parseLong(today) > Long.parseLong(dateToken)){
            return new ResponseEntity<Map>(templateResponse.Error("Your token is expired. Please Get token again."), HttpStatus.OK);
        }
        //update user
        user.setEnabled(true);
        userRepository.save(user);

        return new ResponseEntity<Map>(templateResponse.Success("Sukses, Silahkan Melakukan Login"), HttpStatus.OK);
    }

}

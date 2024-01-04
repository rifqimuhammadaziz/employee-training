package com.rifqimuhammadaziz.employeetraining.controller.user;

import com.rifqimuhammadaziz.employeetraining.config.AppConfig;
import com.rifqimuhammadaziz.employeetraining.model.dao.ForgotPasswordModel;
import com.rifqimuhammadaziz.employeetraining.model.oauth.User;
import com.rifqimuhammadaziz.employeetraining.model.request.EmailRegister;
import com.rifqimuhammadaziz.employeetraining.model.request.ResetPasswordRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.GeneralResponse;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.UserRepository;
import com.rifqimuhammadaziz.employeetraining.service.UserService;
import com.rifqimuhammadaziz.employeetraining.service.email.EmailSender;
import com.rifqimuhammadaziz.employeetraining.utility.EmailTemplate;
import com.rifqimuhammadaziz.employeetraining.utility.SimpleStringUtils;
import com.rifqimuhammadaziz.employeetraining.utility.TemplateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/forgot-password/")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;

    @PostMapping("/send")
    public GeneralResponse<String> sendEmailPassword(@Valid @RequestBody EmailRegister emailRegister) {
        String message = userService.sendEmailReset(emailRegister);
        return new GeneralResponse<>(200, message, "sukses");
    }

    @PostMapping("/validate")
    public GeneralResponse<String> checkTokenValid(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        userService.validateToken(resetPasswordRequest.getOtp());
        return new GeneralResponse<>(200, "success", "success");
    }

    @PostMapping("/change-password")
    public GeneralResponse<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        String message = userService.resetPassword(resetPasswordRequest);
        return new GeneralResponse<>(200, message, "sukses");
    }
}


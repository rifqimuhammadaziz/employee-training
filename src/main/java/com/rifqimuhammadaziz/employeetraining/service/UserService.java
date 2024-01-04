package com.rifqimuhammadaziz.employeetraining.service;

import com.rifqimuhammadaziz.employeetraining.model.oauth.User;
import com.rifqimuhammadaziz.employeetraining.model.request.EmailRegister;
import com.rifqimuhammadaziz.employeetraining.model.request.LoginRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.RegisterRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.ResetPasswordRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.EmailResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.LoginResponse;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

public interface UserService {
    User register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    String sendEmailRegister(EmailRegister emailRegister);

    String validateToken(String token);

    User findUserByOTP(String token);

    void saveUser(User user);

    String sendEmailReset(EmailRegister emailRegister);

    String resetPassword(ResetPasswordRequest resetPasswordRequest);
    EmailResponse<?> repairGoogleSigninAction(MultiValueMap<String, String> params) throws IOException;
}

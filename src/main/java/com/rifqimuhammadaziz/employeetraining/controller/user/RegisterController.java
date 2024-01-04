package com.rifqimuhammadaziz.employeetraining.controller.user;

import com.rifqimuhammadaziz.employeetraining.exception.NotMatchPasswordException;
import com.rifqimuhammadaziz.employeetraining.exception.UserAlreadyExistsException;
import com.rifqimuhammadaziz.employeetraining.exception.UserNotFoundException;
import com.rifqimuhammadaziz.employeetraining.model.oauth.User;
import com.rifqimuhammadaziz.employeetraining.model.request.EmailRegister;
import com.rifqimuhammadaziz.employeetraining.model.request.RegisterRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.GeneralResponse;
import com.rifqimuhammadaziz.employeetraining.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @PostMapping("/register")
    public GeneralResponse<User> saveRegisterManual(@Valid @RequestBody RegisterRequest registerRequest) throws UserAlreadyExistsException, NotMatchPasswordException, UserNotFoundException {
        User oAuthUser = userService.register(registerRequest);
        sendEmailRegister(new EmailRegister(registerRequest.getEmail()));
        return new GeneralResponse<>(200, oAuthUser, "sukses");
    }

    @PostMapping("/send-otp")
    public GeneralResponse<String> sendEmailRegister(@Valid @RequestBody EmailRegister emailRegister) {
        String message = userService.sendEmailRegister(emailRegister);
        return new GeneralResponse<>(200, message, "sukses");
    }

    @GetMapping("/register-confirm-otp/{token}")
    public GeneralResponse<String> confirmOtp(@PathVariable("token") String token) {
        String message = userService.validateToken(token);
        return new GeneralResponse<>(200, message, "sukses");
    }
}

package com.rifqimuhammadaziz.employeetraining.controller.user;

import com.rifqimuhammadaziz.employeetraining.model.dao.LoginModel;
import com.rifqimuhammadaziz.employeetraining.model.dao.RegisterModel;
import com.rifqimuhammadaziz.employeetraining.model.oauth.User;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.UserRepository;
import com.rifqimuhammadaziz.employeetraining.service.UserService;
import com.rifqimuhammadaziz.employeetraining.utility.TemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user-login/")
@Slf4j
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    TemplateResponse templateResponse;

    @PostMapping("/login")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> login(@Valid @RequestBody LoginModel loginModel) {
        User checkUser = userRepository.findOneByUsername(loginModel.getEmail());
        Map map = new HashMap();
        if(checkUser == null) {
            RegisterModel registerModel = new RegisterModel();
            registerModel.setEmail(loginModel.getEmail());
            registerModel.setPassword(loginModel.getPassword());
            registerModel.setConfirmPassword(loginModel.getPassword());
             map = userService.register(registerModel);

        } else {
            map = userService.login(loginModel);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}

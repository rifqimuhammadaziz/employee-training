package com.rifqimuhammadaziz.employeetraining.service;

import com.rifqimuhammadaziz.employeetraining.model.dao.LoginModel;
import com.rifqimuhammadaziz.employeetraining.model.dao.RegisterModel;

import java.util.Map;

public interface UserService {
    Map register(RegisterModel registerModel);
    Map login(LoginModel loginModel);
}

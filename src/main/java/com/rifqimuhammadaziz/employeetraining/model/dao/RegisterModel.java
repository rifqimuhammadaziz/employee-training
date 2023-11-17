package com.rifqimuhammadaziz.employeetraining.model.dao;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterModel {
    public Long id;
    private String email;

    @Size(min = 6, message = "Password required min. 6 character")
    @NotNull(message = "Password is mandatory")
    public String password;

    @Size(min = 6, message = "Password required min. 6 character")
    @NotNull(message = "Password is mandatory")
    public String confirmPassword;

    private String fullName;
}

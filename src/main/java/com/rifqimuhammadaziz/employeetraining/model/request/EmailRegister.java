package com.rifqimuhammadaziz.employeetraining.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRegister {

    @Email(message = "please provide a valid email address")
    private String email;

}
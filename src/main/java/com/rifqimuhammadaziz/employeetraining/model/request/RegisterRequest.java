package com.rifqimuhammadaziz.employeetraining.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email(message = "Please enter a valid email address")
    private String email;

    private String name;

    private String phoneNumber;

    private String domicile;

    private String gender;

    @Size(min = 6, message = "Minimal 6 karakter")
    @NotNull(message = "Password is mandatory")
    private String password;

    @Size(min = 6, message = "Minimal 6 karakter")
    @NotNull(message = "Password is mandatory")
    private String confirmPassword;

    @NotNull(message = "Please provide your full name")
    private String fullName;
}

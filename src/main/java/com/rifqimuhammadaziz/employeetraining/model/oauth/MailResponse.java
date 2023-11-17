package com.rifqimuhammadaziz.employeetraining.model.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailResponse {
    private String message;
    private boolean status;
}

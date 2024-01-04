package com.rifqimuhammadaziz.employeetraining.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponse<T> {
    private int code;
    private T data;
    private String type;
    private String status;
}
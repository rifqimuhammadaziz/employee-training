package com.rifqimuhammadaziz.employeetraining.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GeneralResponse<T> {
    private int code;
    private T data;
    private String status;
}
package com.rifqimuhammadaziz.employeetraining.model.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdRequest {
    @NotNull(message = "Id tidak boleh kosong")
    private Integer id;
}

package com.rifqimuhammadaziz.employeetraining.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTrainingRequest {
    @NotNull(message = "id tidak boleh kosong")
    private Integer id;

    @NotNull(message = "tema tidak boleh kosong")
    private String tema;

    @NotNull(message = "pengajar tidak boleh kosong")
    private String pengajar;
}

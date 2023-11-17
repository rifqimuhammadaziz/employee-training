package com.rifqimuhammadaziz.employeetraining.model.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetailKaryawanRequest {
    @NotNull(message = "nik tidak boleh kosong")
    private String nik;

    @NotNull(message = "npwp tidak boleh kosong")
    private String npwp;
}

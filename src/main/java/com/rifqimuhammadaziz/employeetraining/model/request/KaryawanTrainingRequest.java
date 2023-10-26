package com.rifqimuhammadaziz.employeetraining.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class KaryawanTrainingRequest {
    private IdRequest karyawan;
    private IdRequest training;

    @NotNull(message = "tanggal tidak boleh kosong")
    private Date tanggal;
}

package com.rifqimuhammadaziz.employeetraining.model.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateKaryawanTrainingRequest {
    @NotNull(message = "id tidak boleh kosong")
    private Integer id;

    private IdRequest karyawan;
    private IdRequest training;

    @NotNull(message = "tanggal tidak boleh kosong")
    private Date tanggal;
}

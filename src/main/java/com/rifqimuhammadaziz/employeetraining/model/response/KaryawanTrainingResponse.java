package com.rifqimuhammadaziz.employeetraining.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class KaryawanTrainingResponse extends BaseResponse {
    private Integer id;
    private KaryawanResponse karyawan;
    private TrainingResponse training;
    private Date tanggal;

    public KaryawanTrainingResponse(Date createdDate, Date updatedDate, Date deletedDate, Integer id, KaryawanResponse karyawan, TrainingResponse training, Date tanggal) {
        super(createdDate, updatedDate, deletedDate);
        this.id = id;
        this.karyawan = karyawan;
        this.training = training;
        this.tanggal = tanggal;
    }
}

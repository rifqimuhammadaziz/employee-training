package com.rifqimuhammadaziz.employeetraining.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class KaryawanResponse  extends BaseResponse {
    private Integer id;
    private String nama;
    private String dob;
    private String status;
    private String alamat;
    private DetailKaryawanResponse detailKaryawan;
    public KaryawanResponse(Date createdDate, Date updatedDate, Date deletedDate, Integer id, String nama, Date dob, String status, String alamat, DetailKaryawanResponse detailKaryawan) {
        super(createdDate, updatedDate, deletedDate);
        this.id = id;
        this.nama = nama;
        this.dob = convertDateToString(dob);
        this.status = status;
        this.alamat = alamat;
        this.detailKaryawan = detailKaryawan;
    }
}

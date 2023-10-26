package com.rifqimuhammadaziz.employeetraining.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailKaryawanResponse extends BaseResponse {
    private Integer id;
    private String nik;

    private String npwp;

    public DetailKaryawanResponse(Date createdDate, Date updatedDate, Date deletedDate, Integer id, String nik, String npwp) {
        super(createdDate, deletedDate, updatedDate);
        this.id = id;
        this.nik = nik;
        this.npwp = npwp;
    }
}

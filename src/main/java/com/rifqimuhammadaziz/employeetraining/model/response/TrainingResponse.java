package com.rifqimuhammadaziz.employeetraining.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class TrainingResponse extends BaseResponse{
    private Integer id;
    private String tema;
    private String pengajar;

    public TrainingResponse(Date createdDate, Date updatedDate, Date deletedDate, Integer id, String tema, String pengajar) {
        super(createdDate, updatedDate, deletedDate);
        this.id = id;
        this.tema = tema;
        this.pengajar = pengajar;
    }
}

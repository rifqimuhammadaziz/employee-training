package com.rifqimuhammadaziz.employeetraining.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class BaseResponse {

    private String createdDate;
    private String updatedDate;
    private String deletedDate;

    protected BaseResponse(Date createdDate, Date updatedDate, Date deletedDate) {
        this.createdDate = convertDateToString(createdDate);
        this.updatedDate = convertDateToString(updatedDate);
        this.deletedDate = convertDateToString(deletedDate);
    }

    protected String convertDateToString(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return dateFormat.format(date);
    }
}

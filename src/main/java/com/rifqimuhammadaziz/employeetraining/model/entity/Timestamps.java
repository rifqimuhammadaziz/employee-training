package com.rifqimuhammadaziz.employeetraining.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class Timestamps {
    @Column(name = "sys_created_date")
    private Date createdDate;

    @Column(name = "sys_deleted_date")
    private Date deletedDate;

    @Column(name = "sys_updated_date")
    private Date updatedDate;
}

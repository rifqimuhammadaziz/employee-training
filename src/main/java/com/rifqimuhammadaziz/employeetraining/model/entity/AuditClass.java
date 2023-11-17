package com.rifqimuhammadaziz.employeetraining.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class AuditClass {
    @Column(name = "sys_created_date")
    private Date createdDate;

    @Column(name = "sys_deleted_date")
    private Date deletedDate;

    @Column(name = "sys_updated_date")
    private Date updatedDate;
}

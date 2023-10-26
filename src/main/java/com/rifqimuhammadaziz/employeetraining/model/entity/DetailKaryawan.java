package com.rifqimuhammadaziz.employeetraining.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detail_karyawan")
public class DetailKaryawan extends Timestamps implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nik")
    private String nik;

    @Column(name = "npwp")
    private String npwp;

    @OneToOne(mappedBy = "detailKaryawan", cascade = CascadeType.ALL)
    private Karyawan karyawan;

    public DetailKaryawan(Date createdDate, Date updatedDate, Date deletedDate, String nik, String npwp) {
        this.setCreatedDate(createdDate);
        this.setUpdatedDate(updatedDate);
        this.setDeletedDate(deletedDate);
        this.nik = nik;
        this.npwp = npwp;
    }

    public DetailKaryawan(Date updatedDate, Date deletedDate, String nik, String npwp) {
        this.setUpdatedDate(updatedDate);
        this.setDeletedDate(deletedDate);
        this.nik = nik;
        this.npwp = npwp;
    }

    public DetailKaryawan(Date deletedDate, String nik, String npwp) {
        this.setUpdatedDate(deletedDate);
        this.nik = nik;
        this.npwp = npwp;
    }

}

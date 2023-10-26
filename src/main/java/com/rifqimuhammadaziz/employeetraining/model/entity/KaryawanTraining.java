package com.rifqimuhammadaziz.employeetraining.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "karyawan_training")
public class KaryawanTraining extends Timestamps implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tanggal")
    private Date tanggal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "karyawan_id", referencedColumnName = "id")
    private Karyawan karyawan;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "training_id")
    private Training training;

    public KaryawanTraining(Date tanggal, Karyawan karyawan, Training training, Date createdDate) {
        this.tanggal = tanggal;
        this.karyawan = karyawan;
        this.training = training;
        this.setCreatedDate(createdDate);
    }

}

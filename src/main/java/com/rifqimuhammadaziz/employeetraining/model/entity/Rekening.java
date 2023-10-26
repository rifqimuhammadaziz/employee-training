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
@Table(name = "rekening")
public class Rekening extends Timestamps implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "jenis")
    private String jenis;

    @Column(name = "nama")
    private String nama;

    @Column(name = "rekening")
    private String rekening;

    @Column(name = "alamat")
    private String alamat;

    @ManyToOne
    @JoinColumn(name = "karyawan_id")
    private Karyawan karyawan;

    public Rekening(String jenis, String nama, String rekening, String alamat, Karyawan karyawan, Date createdDate) {
        this.jenis = jenis;
        this.nama = nama;
        this.rekening = rekening;
        this.alamat = alamat;
        this.karyawan = karyawan;
        this.setCreatedDate(createdDate);
    }

}

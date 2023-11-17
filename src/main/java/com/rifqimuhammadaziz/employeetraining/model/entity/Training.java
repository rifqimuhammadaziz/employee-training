package com.rifqimuhammadaziz.employeetraining.model.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "training")
public class Training extends AuditClass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tema")
    private String tema;

    @Column(name = "pengajar")
    private String pengajar;

    @ManyToOne
    @JoinColumn(name = "karyawan_id", referencedColumnName = "id")
    private Karyawan karyawan;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private Set<KaryawanTraining> karyawanTrainings = new HashSet<>();

    public Training(String tema, String pengajar, Date createdDate) {
        this.tema = tema;
        this.pengajar = pengajar;
        this.setCreatedDate(createdDate);
    }
}

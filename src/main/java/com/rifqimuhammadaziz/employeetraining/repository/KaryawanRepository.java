package com.rifqimuhammadaziz.employeetraining.repository;

import com.rifqimuhammadaziz.employeetraining.model.entity.Karyawan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KaryawanRepository extends JpaRepository<Karyawan, Integer> {
}

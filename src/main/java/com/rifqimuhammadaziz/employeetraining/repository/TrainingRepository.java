package com.rifqimuhammadaziz.employeetraining.repository;

import com.rifqimuhammadaziz.employeetraining.model.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
}

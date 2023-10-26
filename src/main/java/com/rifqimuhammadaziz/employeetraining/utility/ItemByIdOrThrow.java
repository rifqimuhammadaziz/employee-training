package com.rifqimuhammadaziz.employeetraining.utility;

import com.rifqimuhammadaziz.employeetraining.model.entity.Karyawan;
import com.rifqimuhammadaziz.employeetraining.model.entity.KaryawanTraining;
import com.rifqimuhammadaziz.employeetraining.model.entity.Rekening;
import com.rifqimuhammadaziz.employeetraining.model.entity.Training;
import com.rifqimuhammadaziz.employeetraining.repository.KaryawanRepository;
import com.rifqimuhammadaziz.employeetraining.repository.KaryawanTrainingRepository;
import com.rifqimuhammadaziz.employeetraining.repository.RekeningRepository;
import com.rifqimuhammadaziz.employeetraining.repository.TrainingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@Component
public class ItemByIdOrThrow {
    public KaryawanTraining findKaryawanTrainingByIdOrThrow(KaryawanTrainingRepository karyawanTrainingRepository, int id) {
        return karyawanTrainingRepository.findById(id).orElseThrow(() -> {
            log.error("Karyawan Training with id {} not found", id);
            throw new NoSuchElementException("Data karyawan training not found");
        });
    }

    public Karyawan findKaryawanByIdOrThrow(KaryawanRepository karyawanRepository, Integer id) {
        return karyawanRepository.findById(id).orElseThrow(() -> {
            log.error("Karyawan with id: {} not found", id);
            throw new NoSuchElementException("Data karyawan not found");
        });
    }

    public Training findTrainingByIdOrThrow(TrainingRepository trainingRepository, int id) {
        return trainingRepository.findById(id).orElseThrow(() -> {
            log.error("Training with id: {} not found", id);
            throw new NoSuchElementException("Data training not found");
        });
    }

    public Rekening findRekeningByIdOrThrow(RekeningRepository rekeningRepository, int id) {
        return rekeningRepository.findById(id).orElseThrow(() -> {
            log.error("Rekening with id: {} not found", id);
            throw new NoSuchElementException("Data rekening not found");
        });
    }
}

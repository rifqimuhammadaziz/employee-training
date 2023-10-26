package com.rifqimuhammadaziz.employeetraining.service;

import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.KaryawanTrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateKaryawanTrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.KaryawanTrainingResponse;
import org.springframework.data.domain.Page;

public interface KaryawanTrainingService {
    KaryawanTrainingResponse insertKaryawanTraining(KaryawanTrainingRequest trainingRequest);

    KaryawanTrainingResponse getKaryawanTraining(int id);

    KaryawanTrainingResponse updateKaryawanTraining(UpdateKaryawanTrainingRequest updateKaryawanTrainingRequest);

    Page<KaryawanTrainingResponse> getAllKaryawanTraining(int page, int size);

    void deleteKaryawanTraining(IdRequest idRequest);

}

package com.rifqimuhammadaziz.employeetraining.service;

import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.TrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateTrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.TrainingResponse;
import org.springframework.data.domain.Page;

public interface TrainingService {
    TrainingResponse insertTraining(TrainingRequest trainingRequest);
    TrainingResponse getTraining(Integer id);
    TrainingResponse updateTraining(UpdateTrainingRequest updateTrainingRequest);
    Page<TrainingResponse> getTrainings(int page, int size);
    void deleteTraining(IdRequest idRequest);
}

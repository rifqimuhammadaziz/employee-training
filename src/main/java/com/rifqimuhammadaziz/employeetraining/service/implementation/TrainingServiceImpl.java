package com.rifqimuhammadaziz.employeetraining.service.implementation;

import com.rifqimuhammadaziz.employeetraining.model.entity.Training;
import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.TrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateTrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.TrainingResponse;
import com.rifqimuhammadaziz.employeetraining.repository.TrainingRepository;
import com.rifqimuhammadaziz.employeetraining.service.TrainingService;
import com.rifqimuhammadaziz.employeetraining.utility.ItemByIdOrThrow;
import com.rifqimuhammadaziz.employeetraining.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final ValidationUtility validationUtility;
    private final ItemByIdOrThrow itemByIdOrThrow;

    @Override
    public TrainingResponse insertTraining(TrainingRequest trainingRequest) {
        validationUtility.validate(trainingRequest);
        Training training = new Training(
                trainingRequest.getTema(),
                trainingRequest.getPengajar(),
                new Date()
        );

        Training trainingRepo = trainingRepository.save(training);
        log.info("Successfully saved training");
        return convertToResponse(trainingRepo);
    }

    @Override
    public TrainingResponse getTraining(Integer id) {
        Training training = findTrainingByIdOrThrow(id);
        log.info("Successfully get training by id {}", id);
        return convertToResponse(training);
    }

    @Override
    public TrainingResponse updateTraining(UpdateTrainingRequest updateTrainingRequest) {
        validationUtility.validate(updateTrainingRequest);
        Training training = findTrainingByIdOrThrow(updateTrainingRequest.getId());
        // update training field
        training.setTema(updateTrainingRequest.getTema());
        training.setPengajar(updateTrainingRequest.getPengajar());
        training.setUpdatedDate(new Date());

        trainingRepository.save(training);
        log.info("Successfully updated training");
        return convertToResponse(training);
    }

    @Override
    public Page<TrainingResponse> getTrainings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TrainingResponse> trainingResponses = trainingRepository.findAll(pageable).getContent().stream().map(this::convertToResponse).toList();

        log.info("Successfully get all training");
        return new PageImpl<>(trainingResponses);
    }

    @Override
    public void deleteTraining(IdRequest idRequest) {
        Training training = findTrainingByIdOrThrow(idRequest.getId());
        log.info("Deleted training with id {}", training.getId());
        trainingRepository.delete(training);
    }

    private Training findTrainingByIdOrThrow(int id) {
        return itemByIdOrThrow.findTrainingByIdOrThrow(trainingRepository, id);
    }

    private TrainingResponse convertToResponse(Training training) {
        return new TrainingResponse(
                training.getCreatedDate(),
                training.getUpdatedDate(),
                training.getDeletedDate(),
                training.getId(),
                training.getTema(),
                training.getPengajar()
        );
    }
}

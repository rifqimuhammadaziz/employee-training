package com.rifqimuhammadaziz.employeetraining.controller;

import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.TrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateTrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.GeneralResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.TrainingResponse;
import com.rifqimuhammadaziz.employeetraining.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public GeneralResponse<TrainingResponse> insertTraining(@RequestBody @Valid TrainingRequest trainingRequest) {
        TrainingResponse trainingResponse = trainingService.insertTraining(trainingRequest);
        return new GeneralResponse<>(
                200,
                trainingResponse,
                "success"
        );
    }

    @GetMapping("/{id}")
    public GeneralResponse<TrainingResponse> getTraining(@PathVariable("id") int id) {
        TrainingResponse trainingResponse = trainingService.getTraining(id);
        return new GeneralResponse<>(
                200,
                trainingResponse,
                "success"
        );
    }

    @PutMapping
    public GeneralResponse<TrainingResponse> updateTraining(@RequestBody @Valid UpdateTrainingRequest updateTrainingRequest) {
        TrainingResponse trainingResponse = trainingService.updateTraining(updateTrainingRequest);
        return new GeneralResponse<>(
                200,
                trainingResponse,
                "success"
        );
    }

    @GetMapping
    public GeneralResponse<Page<TrainingResponse>> getAllTraining(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TrainingResponse> trainingResponses = trainingService.getTrainings(page, size);
        return new GeneralResponse<>(
                200,
                trainingResponses,
                "success"
        );
    }

    @DeleteMapping
    public GeneralResponse<String> deleteTraining(@RequestBody @Valid IdRequest idRequest) {
        trainingService.deleteTraining(idRequest);
        return new GeneralResponse<>(
                200,
                "success",
                "success"
        );
    }
}

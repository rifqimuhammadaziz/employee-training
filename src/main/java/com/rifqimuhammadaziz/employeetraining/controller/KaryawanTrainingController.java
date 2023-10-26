package com.rifqimuhammadaziz.employeetraining.controller;

import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.KaryawanTrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateKaryawanTrainingRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.GeneralResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.KaryawanTrainingResponse;
import com.rifqimuhammadaziz.employeetraining.service.KaryawanTrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/karyawan-trainings")
@RequiredArgsConstructor
public class KaryawanTrainingController {

    private final KaryawanTrainingService karyawanTrainingService;

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public GeneralResponse<KaryawanTrainingResponse> insertKaryawanTraining(@RequestBody @Valid KaryawanTrainingRequest karyawanTrainingRequest) {
        KaryawanTrainingResponse karyawanTrainingResponse = karyawanTrainingService.insertKaryawanTraining(karyawanTrainingRequest);
        return new GeneralResponse<>(
                200,
                karyawanTrainingResponse,
                "success"
        );
    }

    @GetMapping("/{id}")
    public GeneralResponse<KaryawanTrainingResponse> getTraining(@PathVariable("id") int id) {
        KaryawanTrainingResponse trainingResponse = karyawanTrainingService.getKaryawanTraining(id);
        return new GeneralResponse<>(
                200,
                trainingResponse,
                "success"
        );
    }

    @PutMapping
    public GeneralResponse<KaryawanTrainingResponse> updateTraining(@RequestBody @Valid UpdateKaryawanTrainingRequest updateKaryawanTrainingRequest) {
        KaryawanTrainingResponse karyawanTrainingResponse = karyawanTrainingService.updateKaryawanTraining(updateKaryawanTrainingRequest);
        return new GeneralResponse<>(
                200,
                karyawanTrainingResponse,
                "success"
        );
    }

    @GetMapping
    public GeneralResponse<Page<KaryawanTrainingResponse>> getAllKaryawanTraining(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<KaryawanTrainingResponse> karyawanTrainingResponses = karyawanTrainingService.getAllKaryawanTraining(page, size);
        return new GeneralResponse<>(
                200,
                karyawanTrainingResponses,
                "success"
        );
    }

    @DeleteMapping
    public GeneralResponse<String> deleteTraining(@RequestBody @Valid IdRequest idRequest) {
        karyawanTrainingService.deleteKaryawanTraining(idRequest);
        return new GeneralResponse<>(
                200,
                "success",
                "success"
        );
    }
}

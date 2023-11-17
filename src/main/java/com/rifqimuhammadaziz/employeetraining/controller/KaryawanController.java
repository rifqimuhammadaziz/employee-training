package com.rifqimuhammadaziz.employeetraining.controller;

import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.KaryawanRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateKaryawanRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.GeneralResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.KaryawanResponse;
import com.rifqimuhammadaziz.employeetraining.service.KaryawanService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/karyawans")
@RequiredArgsConstructor
public class KaryawanController {

    private final KaryawanService karyawanService;

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public GeneralResponse<KaryawanResponse> insertKaryawan(@RequestBody @Valid KaryawanRequest karyawanRequest) {
        KaryawanResponse karyawanResponse = karyawanService.insertKaryawan(karyawanRequest);

        return new GeneralResponse<>(
                200,
                karyawanResponse,
                "success"
        );
    }

    @GetMapping("/{id}")
    public GeneralResponse<KaryawanResponse> getKaryawan(@PathVariable("id") Integer id) {
        KaryawanResponse karyawanResponse = karyawanService.getKaryawan(id);

        return new GeneralResponse<>(
                200,
                karyawanResponse,
                "success"
        );
    }

    @PutMapping
    public GeneralResponse<KaryawanResponse> updateKaryawan(@RequestBody @Valid UpdateKaryawanRequest updateKaryawanRequest) {
        KaryawanResponse karyawanResponse = karyawanService.updateKaryawan(updateKaryawanRequest);
        return new GeneralResponse<>(
                200,
                karyawanResponse,
                "success"
        );
    }

    @GetMapping
    public GeneralResponse<Page<KaryawanResponse>> getAllKaryawan(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<KaryawanResponse> karyawanResponse = karyawanService.getAllKaryawan(page, size);
        return new GeneralResponse<>(
                200,
                karyawanResponse,
                "success"
        );
    }

    @DeleteMapping
    public GeneralResponse<String> deleteKaryawan(@RequestBody @Valid IdRequest idRequest) {
        karyawanService.deleteKaryawan(idRequest);
        return new GeneralResponse<>(
                200,
                "success",
                "success"
        );
    }
}
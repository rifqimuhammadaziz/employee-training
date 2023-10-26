package com.rifqimuhammadaziz.employeetraining.controller;

import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.RekeningRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateRekeningRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.GeneralResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.RekeningResponse;
import com.rifqimuhammadaziz.employeetraining.service.RekeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rekenings")
@RequiredArgsConstructor
public class RekeningController {

    private final RekeningService rekeningService;

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public GeneralResponse<RekeningResponse> insertRekening(@RequestBody @Valid RekeningRequest rekeningRequest) {
        RekeningResponse rekeningResponse = rekeningService.insertRekening(rekeningRequest);
        return new GeneralResponse<>(
                200,
                rekeningResponse,
                "success"
        );
    }

    @GetMapping("/{id}")
    public GeneralResponse<RekeningResponse> getRekening(@PathVariable("id") int id) {
        RekeningResponse rekeningResponse = rekeningService.getRekening(id);
        return new GeneralResponse<>(
                200,
                rekeningResponse,
                "success"
        );
    }

    @PutMapping
    public GeneralResponse<RekeningResponse> updateRekening(@RequestBody @Valid UpdateRekeningRequest updateRekeningRequest) {
        RekeningResponse rekeningResponse = rekeningService.updateRekening(updateRekeningRequest);
        return new GeneralResponse<>(
                200,
                rekeningResponse,
                "success"
        );
    }

    @GetMapping
    public GeneralResponse<Page<RekeningResponse>> getAllRekening(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RekeningResponse> rekeningResponses = rekeningService.getAllRekening(page, size);
        return new GeneralResponse<>(
                200,
                rekeningResponses,
                "success"
        );
    }

    @DeleteMapping
    public GeneralResponse<String> deleteRekening(@RequestBody @Valid IdRequest idRequest) {
        rekeningService.deleteRekening(idRequest);
        return new GeneralResponse<>(
                200,
                "success",
                "success"
        );
    }
}

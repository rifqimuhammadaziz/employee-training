package com.rifqimuhammadaziz.employeetraining.service;


import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.RekeningRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateRekeningRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.RekeningResponse;
import org.springframework.data.domain.Page;

public interface RekeningService {
    RekeningResponse insertRekening(RekeningRequest rekeningRequest);
    RekeningResponse getRekening(int id);
    RekeningResponse updateRekening(UpdateRekeningRequest updateRekeningRequest);
    void deleteRekening(IdRequest idRequest);
    Page<RekeningResponse> getAllRekening(int page, int size);
}

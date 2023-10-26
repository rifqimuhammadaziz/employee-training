package com.rifqimuhammadaziz.employeetraining.service;

import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.KaryawanRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateKaryawanRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.KaryawanResponse;
import org.springframework.data.domain.Page;

public interface KaryawanService {
    KaryawanResponse insertKaryawan(KaryawanRequest karyawanRequest);
    KaryawanResponse getKaryawan(Integer id);
    KaryawanResponse updateKaryawan(UpdateKaryawanRequest updateKaryawanRequest);
    Page<KaryawanResponse> getAllKaryawan(int page, int size);
    void deleteKaryawan(IdRequest idRequest);
}

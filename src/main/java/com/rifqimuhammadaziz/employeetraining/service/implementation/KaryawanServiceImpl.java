package com.rifqimuhammadaziz.employeetraining.service.implementation;

import com.rifqimuhammadaziz.employeetraining.model.entity.DetailKaryawan;
import com.rifqimuhammadaziz.employeetraining.model.entity.Karyawan;
import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.KaryawanRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateKaryawanRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.DetailKaryawanResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.KaryawanResponse;
import com.rifqimuhammadaziz.employeetraining.repository.KaryawanRepository;
import com.rifqimuhammadaziz.employeetraining.service.KaryawanService;
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
public class KaryawanServiceImpl implements KaryawanService {

    private final KaryawanRepository karyawanRepository;
    private final ValidationUtility validationUtility;
    private final ItemByIdOrThrow itemByIdOrThrow;

    @Override
    public KaryawanResponse insertKaryawan(KaryawanRequest karyawanRequest) {
        validationUtility.validate(karyawanRequest);

        Karyawan karyawan = new Karyawan(
                karyawanRequest.getNama(),
                karyawanRequest.getDob(),
                karyawanRequest.getStatus(),
                karyawanRequest.getAlamat(),
                new DetailKaryawan(
                        new Date(),
                        null,
                        null,
                        karyawanRequest.getDetailKaryawan().getNik(),
                        karyawanRequest.getDetailKaryawan().getNpwp()
                ),
                new Date()
        );
        Karyawan karyawanRepo = karyawanRepository.save(karyawan);
        log.info("Successfully saved karyawan");
        return convertToResponse(karyawanRepo);
    }

    @Override
    public KaryawanResponse getKaryawan(Integer id) {
        Karyawan karyawan = findKaryawanByIdOrThrow(id);

        log.info("Successfully get karyawan by id {}", id);
        return convertToResponse(karyawan);
    }

    @Override
    public KaryawanResponse updateKaryawan(UpdateKaryawanRequest updateKaryawanRequest) {
        validationUtility.validate(updateKaryawanRequest);
        Karyawan karyawan = findKaryawanByIdOrThrow(updateKaryawanRequest.getId());

        // update the karyawan field
        karyawan.setNama(updateKaryawanRequest.getNama());
        karyawan.setDob(updateKaryawanRequest.getDob());
        karyawan.setStatus(updateKaryawanRequest.getStatus());
        karyawan.setAlamat(updateKaryawanRequest.getAlamat());
        karyawan.setUpdatedDate(new Date());
        karyawan.getDetailKaryawan().setNik(updateKaryawanRequest.getDetailKaryawan().getNik());
        karyawan.getDetailKaryawan().setNpwp(updateKaryawanRequest.getDetailKaryawan().getNpwp());
        karyawan.getDetailKaryawan().setUpdatedDate(new Date());
        karyawanRepository.save(karyawan);
        log.info("Successfully updated karyawan");
        return convertToResponse(karyawan);
    }

    @Override
    public Page<KaryawanResponse> getAllKaryawan(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<KaryawanResponse> data = karyawanRepository.findAll(paging).getContent().stream().map(this::convertToResponse).toList();

        log.info("Successfully get all karyawan");
        return new PageImpl<>(data);
    }

    @Override
    public void deleteKaryawan(IdRequest idRequest) {
        validationUtility.validate(idRequest);
        Karyawan karyawan = findKaryawanByIdOrThrow(idRequest.getId());
        log.info("Deleted karyawan with id {}", karyawan.getId());
        karyawanRepository.delete(karyawan);
    }

    private Karyawan findKaryawanByIdOrThrow(Integer id) {
        return itemByIdOrThrow.findKaryawanByIdOrThrow(karyawanRepository, id);
    }

    private KaryawanResponse convertToResponse(Karyawan karyawan) {
        return new KaryawanResponse(
                karyawan.getCreatedDate(),
                karyawan.getUpdatedDate(),
                karyawan.getDeletedDate(),
                karyawan.getId(),
                karyawan.getNama(),
                karyawan.getDob(),
                karyawan.getStatus(),
                karyawan.getAlamat(),
                new DetailKaryawanResponse(
                        karyawan.getDetailKaryawan().getCreatedDate(),
                        karyawan.getDetailKaryawan().getDeletedDate(),
                        karyawan.getDetailKaryawan().getUpdatedDate(),
                        karyawan.getDetailKaryawan().getId(),
                        karyawan.getDetailKaryawan().getNik(),
                        karyawan.getDetailKaryawan().getNpwp()
                )
        );
    }
}

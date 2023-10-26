package com.rifqimuhammadaziz.employeetraining.service.implementation;

import com.rifqimuhammadaziz.employeetraining.model.entity.Karyawan;
import com.rifqimuhammadaziz.employeetraining.model.entity.Rekening;
import com.rifqimuhammadaziz.employeetraining.model.request.IdRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.RekeningRequest;
import com.rifqimuhammadaziz.employeetraining.model.request.UpdateRekeningRequest;
import com.rifqimuhammadaziz.employeetraining.model.response.KaryawanRekeningResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.RekeningResponse;
import com.rifqimuhammadaziz.employeetraining.repository.KaryawanRepository;
import com.rifqimuhammadaziz.employeetraining.repository.RekeningRepository;
import com.rifqimuhammadaziz.employeetraining.service.RekeningService;
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
public class RekeningServiceImpl implements RekeningService {

    private final RekeningRepository rekeningRepository;
    private final KaryawanRepository karyawanRepository;
    private final ValidationUtility validationUtility;
    private final ItemByIdOrThrow itemByIdOrThrow;

    @Override
    public RekeningResponse insertRekening(RekeningRequest rekeningRequest) {
        validationUtility.validate(rekeningRequest);

        Karyawan karyawan = findKaryawanByIdOrThrow(rekeningRequest.getKaryawan().getId());

        Rekening rekening = new Rekening(
                rekeningRequest.getJenis(),
                rekeningRequest.getNama(),
                rekeningRequest.getRekening(),
                rekeningRequest.getAlamat(),
                karyawan,
                new Date()
        );
        return convertToResponse(rekeningRepository.save(rekening));
    }

    @Override
    public RekeningResponse getRekening(int id) {
        validationUtility.validate(id);
        Rekening rekening = findRekeningByIdOrThrow(id);
        log.info("Successfully get training with id {}", id);
        return convertToResponse(rekening);
    }

    @Override
    public RekeningResponse updateRekening(UpdateRekeningRequest updateRekeningRequest) {
        validationUtility.validate(updateRekeningRequest);
        Rekening rekening = findRekeningByIdOrThrow(updateRekeningRequest.getId());
        Karyawan karyawan = findKaryawanByIdOrThrow(updateRekeningRequest.getKaryawan().getId());

        // update the field rekening
        rekening.setNama(updateRekeningRequest.getNama());
        rekening.setJenis(updateRekeningRequest.getJenis());
        rekening.setRekening(updateRekeningRequest.getRekening());
        rekening.setAlamat(updateRekeningRequest.getAlamat());
        rekening.setUpdatedDate(new Date());
        rekening.setKaryawan(karyawan);

        rekeningRepository.save(rekening);
        log.info("Successfully updated rekening data");
        return convertToResponse(rekening);
    }

    @Override
    public Page<RekeningResponse> getAllRekening(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<RekeningResponse> rekeningResponses = rekeningRepository.findAll(pageable).getContent().stream().map(this::convertToResponse).toList();

        log.info("Successfully get all rekening");
        return new PageImpl<>(rekeningResponses);
    }

    @Override
    public void deleteRekening(IdRequest idRequest) {
        validationUtility.validate(idRequest);
        Rekening rekening = findRekeningByIdOrThrow(idRequest.getId());
        log.info("Deleted Rekening with id {}", idRequest.getId());
        rekeningRepository.delete(rekening);
    }

    private Rekening findRekeningByIdOrThrow(int id) {
        return itemByIdOrThrow.findRekeningByIdOrThrow(rekeningRepository, id);
    }

    private Karyawan findKaryawanByIdOrThrow(Integer id) {
        return itemByIdOrThrow.findKaryawanByIdOrThrow(karyawanRepository, id);
    }

    private RekeningResponse convertToResponse(Rekening rekening) {
        return new RekeningResponse(
                rekening.getCreatedDate(),
                rekening.getUpdatedDate(),
                rekening.getDeletedDate(),
                rekening.getId(),
                rekening.getNama(),
                rekening.getJenis(),
                rekening.getRekening(),
                rekening.getAlamat(),
                new KaryawanRekeningResponse(
                        rekening.getKaryawan().getId().toString(),
                        rekening.getKaryawan().getNama()
                )
        );
    }

}

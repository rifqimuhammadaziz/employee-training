package com.rifqimuhammadaziz.employeetraining.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RekeningRequest {
    @NotNull(message = "nama tidak boleh kosong")
    private String nama;

    @NotNull(message = "rekening tidak boleh kosong")
    private String jenis;

    @NotNull(message = "nomor rekening tidak boleh kosong")
    private String rekening;

    @NotNull(message = "alamat harus diisi")
    private String alamat;

    private IdRequest karyawan;
}
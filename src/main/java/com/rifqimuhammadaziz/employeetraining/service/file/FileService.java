package com.rifqimuhammadaziz.employeetraining.service.file;

import com.rifqimuhammadaziz.employeetraining.model.response.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    UploadFileResponse uploadFile(MultipartFile file);
    Resource loadFileAsResource(String fileName);
}
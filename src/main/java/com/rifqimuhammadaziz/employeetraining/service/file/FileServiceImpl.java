package com.rifqimuhammadaziz.employeetraining.service.file;

import com.rifqimuhammadaziz.employeetraining.config.properties.FileStorageProperties;
import com.rifqimuhammadaziz.employeetraining.exception.FileStorageException;
import com.rifqimuhammadaziz.employeetraining.model.response.UploadFileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation;

    @Value("${app.uploadto.cdn}")
    private String UPLOADED_FOLDER;

    public FileServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception exception) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be storage", exception);
        }
    }

    @Override
    public UploadFileResponse uploadFile(MultipartFile file) {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
        String strDate = dateFormat.format(date);

        String nameFormat = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        if (nameFormat.isEmpty()) {
            nameFormat = ".png";
        }
        String fileName = UPLOADED_FOLDER + strDate + nameFormat;
        String fileNameForDownload = strDate + nameFormat;
        Path to = Paths.get(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, to);
        } catch (Exception exception) {
            log.error("Could not copy file", exception);
            return new UploadFileResponse(fileName, null, file.getContentType(), file.getSize(), exception.getMessage());
        }

        String fileDownloadUri =
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/file/show-file/")
                        .path(fileNameForDownload)
                        .toUriString();

        log.info("Successfully uploaded file {}", fileName);
        return new UploadFileResponse(fileNameForDownload, fileDownloadUri, file.getContentType(), file.getSize(), "false");
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        Resource resource;
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException(String.format("File not found %s", fileName));
            }
        } catch (MalformedURLException exception) {
            throw new FileStorageException(String.format("File not found %s", fileName), exception);
        }
    }
}
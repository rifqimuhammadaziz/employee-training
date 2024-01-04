package com.rifqimuhammadaziz.employeetraining.controller.uploadfile;

import com.rifqimuhammadaziz.employeetraining.model.response.GeneralResponse;
import com.rifqimuhammadaziz.employeetraining.model.response.UploadFileResponse;
import com.rifqimuhammadaziz.employeetraining.service.file.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/file")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public GeneralResponse<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        UploadFileResponse response = fileService.uploadFile(file);
        return new GeneralResponse<>(200, response, "sukses");
    }

    @GetMapping("/show-file/{fileName:.+}")
    public ResponseEntity<Resource> showFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileService.loadFileAsResource(fileName);
        String contentType = "application/octet-stream";

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception e) {
            log.info("Could not determine file type.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}

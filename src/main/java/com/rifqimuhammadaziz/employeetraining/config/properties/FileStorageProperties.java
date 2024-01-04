package com.rifqimuhammadaziz.employeetraining.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileStorageProperties {

    @Value("${app.uploadto.cdn}")
    private String uploadDir;
}
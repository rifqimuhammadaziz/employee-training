package com.rifqimuhammadaziz.employeetraining;

import com.rifqimuhammadaziz.employeetraining.controller.uploadfile.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.rifqimuhammadaziz.employeetraining")
@EnableScheduling
@EnableConfigurationProperties({FileStorageProperties.class})
public class EmployeeTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeTrainingApplication.class, args);
	}

}

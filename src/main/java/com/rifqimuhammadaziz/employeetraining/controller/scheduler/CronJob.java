package com.rifqimuhammadaziz.employeetraining.controller.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Component
@Slf4j
public class CronJob {

    @Autowired
    @Qualifier(value = "taskExecutor")
    private TaskExecutor taskExecutor;

//    @Scheduled(cron = "${cron.expression}")
    public void sendAsync() {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                log.info("[===== CRONJOB =====][Method executed every 10 seconds. Current time is: " + new Date());
            }
        });
    }
}

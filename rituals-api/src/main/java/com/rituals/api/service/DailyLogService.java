package com.rituals.api.service;

import com.rituals.api.model.DailyLog;
import com.rituals.api.model.Task;
import com.rituals.api.repository.DailyLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;

    public DailyLog getOrCreateLogForToday(String userId){
        LocalDate today=LocalDate.now();
        String customId=userId+"_"+today.toString().replace("-","");
        return dailyLogRepository.findById(customId).orElseGet(()->{
            log.info("Creating new daily log for user: {} on date: {},",userId,today);
            DailyLog newLog=DailyLog.builder()
                    .id(customId)
                    .userId(userId)
                    .logData(today)
                    .build();
            return dailyLogRepository.save(newLog);
        });
    }
    public DailyLog addTaskToLog(String userId, LocalDate date, Task task){
        String customId = userId + "_" + date.toString().replace("-", "");

        DailyLog dailyLog = dailyLogRepository.findById(customId)
                .orElseThrow(() -> new RuntimeException("Log not found for date: " + date));

        dailyLog.getTasks().add(task);
        log.info("Added task '{}' to log {}", task.getTitle(), customId);

        return dailyLogRepository.save(dailyLog);
    }
}

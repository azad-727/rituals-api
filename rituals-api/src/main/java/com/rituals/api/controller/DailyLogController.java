package com.rituals.api.controller;

import com.rituals.api.model.DailyLog;
import com.rituals.api.model.Task;
import com.rituals.api.repository.DailyLogRepository;
import com.rituals.api.service.DailyLogService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rituals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DailyLogController {
    private final DailyLogService dailyLogService;
    private final DailyLogRepository dailyLogRepository;

    @GetMapping("/today/{userId}")
    public ResponseEntity<DailyLog> getTodayLog(@PathVariable String userId){
        DailyLog log=dailyLogService.getOrCreateLogForToday(userId);
        return ResponseEntity.ok(log);
    }
    @GetMapping("history/{userId}")
    public ResponseEntity<List<DailyLog>> getUserHistory(@PathVariable String userId){
        List<DailyLog> history=dailyLogRepository.findAllByUserIdOrderByLogDataAsc(userId);
        return ResponseEntity.ok(history);
    }
    @PutMapping("/{userId}/{date}")
    public ResponseEntity<DailyLog> updateDailyTasks(
            @PathVariable("userId") String userId,
            @PathVariable("date") String date,
            @RequestBody java.util.List<Task> updatedTasks) {

        String customId = userId + "_" + date.replace("-", "");

        DailyLog dailyLog = dailyLogRepository.findById(customId)
                .orElseThrow(() -> new RuntimeException("Log not found for date: " + date));

        dailyLog.setTasks(updatedTasks);
        DailyLog savedLog = dailyLogRepository.save(dailyLog);

        return ResponseEntity.ok(savedLog);
    }
    @PostMapping("/{userId}/{date}/tasks")
    public ResponseEntity<DailyLog> addTask(
            @PathVariable("userId") String userId,
            @PathVariable("date") String date, // Expected format: YYYY-MM-DD
            @RequestBody Task task) {

        LocalDate parsedDate = LocalDate.parse(date);
        DailyLog updatedLog = dailyLogService.addTaskToLog(userId, parsedDate, task);

        return ResponseEntity.ok(updatedLog);
    }


}

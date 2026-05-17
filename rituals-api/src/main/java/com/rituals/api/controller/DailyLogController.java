package com.rituals.api.controller;

import com.rituals.api.model.DailyLog;
import com.rituals.api.model.Task;
import com.rituals.api.service.DailyLogService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/rituals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DailyLogController {
    private final DailyLogService dailyLogService;

    @GetMapping("/today/{userId}")
    public ResponseEntity<DailyLog> getTodayLog(@PathVariable String userId){
        DailyLog log=dailyLogService.getOrCreateLogForToday(userId);
        return ResponseEntity.ok(log);
    }

    @PostMapping("/{userId}/{data}/tasks")
    public ResponseEntity<DailyLog> addTask(
            @PathVariable String userId,
            @PathVariable String date, // Expected format: YYYY-MM-DD
            @RequestBody Task task) {

        LocalDate parsedDate = LocalDate.parse(date);
        DailyLog updatedLog = dailyLogService.addTaskToLog(userId, parsedDate, task);

        return ResponseEntity.ok(updatedLog);
    }


}

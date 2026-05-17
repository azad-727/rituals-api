package com.rituals.api.service;

import com.rituals.api.model.DailyLog;
import com.rituals.api.model.RoutineTemplate;
import com.rituals.api.model.Task;
import com.rituals.api.repository.DailyLogRepository;
import com.rituals.api.repository.RoutineTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;
    private final RoutineTemplateRepository routineTemplateRepository; // Inject the new repository

    /**
     * LAZY INSTANTIATION: Initializes a new daily log by reading the Master Template.
     */
    public DailyLog getOrCreateLogForToday(String userId) {
        LocalDate today = LocalDate.now();
        String customId = userId + "_" + today.toString().replace("-", "");

        return dailyLogRepository.findById(customId).orElseGet(() -> {
            log.info("Lazy instantiating daily log for user: {} on {}", userId, today);

            List<Task> todaysTasks = new ArrayList<>();
            Optional<RoutineTemplate> templateOpt = routineTemplateRepository.findByUserId(userId);

            // If the user has a master template, fetch today's specific schedule
            if (templateOpt.isPresent()) {
                String dayOfWeek = today.getDayOfWeek().name(); // Outputs "MONDAY", "TUESDAY", etc.
                List<Task> scheduledTasks = templateOpt.get().getWeeklySchedule().get(dayOfWeek);

                if (scheduledTasks != null) {
                    // Deep copy the tasks so we don't accidentally link database references
                    // and ensure all tasks start with isCompleted = false
                    todaysTasks = scheduledTasks.stream().map(t -> {
                        Task newTask = new Task();
                        newTask.setTitle(t.getTitle());
                        newTask.setEstimatedMinutes(t.getEstimatedMinutes());
                        newTask.setCategory(t.getCategory());
                        // default completion is already false, but enforcing it:
                        newTask.setCompleted(false);
                        return newTask;
                    }).collect(Collectors.toList());
                }
            }

            DailyLog newLog = DailyLog.builder()
                    .id(customId)
                    .userId(userId)
                    .logData(today)
                    .tasks(todaysTasks)
                    .build();

            return dailyLogRepository.save(newLog);
        });
    }

    // Keep our existing add task method for ad-hoc tasks
    public DailyLog addTaskToLog(String userId, LocalDate date, Task task) {
        String customId = userId + "_" + date.toString().replace("-", "");
        DailyLog dailyLog = dailyLogRepository.findById(customId)
                .orElseThrow(() -> new RuntimeException("Log not found for date: " + date));

        dailyLog.getTasks().add(task);
        return dailyLogRepository.save(dailyLog);
    }
}
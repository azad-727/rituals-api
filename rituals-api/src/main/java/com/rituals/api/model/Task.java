package com.rituals.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Task {
    private  String title;

    private boolean isCompleted = false;
    private int estimatedMinutes;
    private int actualMinutesSpent = 0;
    private int focusBreaches = 0;
    private List<String> microLogs = new ArrayList<>();

    private String startTime;
    private String endTime;
    private String category;
    private String emoji;
}

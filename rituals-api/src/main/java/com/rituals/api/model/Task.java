package com.rituals.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
public class Task {
    private  String title;

    private boolean isCompleted = false;
    private int estimatedMinutes;
    private String startTime;
    private String endTime;
    private String category;
}

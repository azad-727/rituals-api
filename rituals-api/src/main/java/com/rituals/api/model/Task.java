package com.rituals.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "task")
public class Task {
    private  String title;
    @Builder.Default
    private boolean isCompleted = false;
    private int estimatedMinutes;
    private String category;
}

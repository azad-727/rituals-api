package com.rituals.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection ="DailyLog")
public class DailyLog {
    @Id
    private String id;
    private String userId;
    private LocalDate logData;

    @Builder.Default
    private List<Task> tasks=new ArrayList<>();
    private String aiMotivationalInsights;

    @Builder.Default
    private double burnoutRiskScore = 0.0;

    private String dayEmoji;

}

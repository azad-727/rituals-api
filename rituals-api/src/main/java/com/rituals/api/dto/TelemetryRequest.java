package com.rituals.api.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TelemetryRequest {

    private String totalHours;
    private int currentStreak;
    private int totalBreaches;
    private int integrityScore;
    private int completionRate;
    private Map<String,Integer> categoryMinutes;
}

package com.rituals.api.controller;

import com.rituals.api.dto.TelemetryRequest;
import com.rituals.api.service.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/oracle")
@CrossOrigin(origins = "http://localhost:5173") // Allow React to talk to this endpoint
public class OracleController {

    @Autowired
    private OracleService oracleService;

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyzeTelemetry(@RequestBody TelemetryRequest request) {
        String analysis = oracleService.generateNeuralProjection(request);

        // Return as a JSON object so React can easily read it
        return ResponseEntity.ok(Map.of("projection", analysis));
    }
}
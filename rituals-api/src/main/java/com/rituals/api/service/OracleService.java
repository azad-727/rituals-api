package com.rituals.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rituals.api.dto.TelemetryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OracleService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateNeuralProjection(TelemetryRequest stats) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            // 1. Convert user stats to a string for the prompt
            String userData = mapper.writeValueAsString(stats);

            // 2. The Master System Prompt (Using String.format to safely inject userData)
            String promptTemplate = """
                You are a brutalist, elite AI performance coach for software engineers.
                Analyze the following user telemetry.
                
                USER DATA:
                %s
                
                You MUST output your analysis as a strict JSON object with EXACTLY these 5 keys. Do not include markdown blocks (```json).
                1. "identity_archetype": A short, intense title based on their highest-volume categories.
                2. "global_benchmark": A brutally honest comparison of their stats against elite developers.
                3. "extracted_skills": A JSON array of 3-4 professional skills they acquired based on their task logs.
                4. "system_warnings": A harsh warning if focus breaches are high. If perfect, praise them.
                5. "the_marginal_edge": One highly specific, actionable micro-habit they can implement tomorrow.
                """;

            // Safely inject the data into the %s placeholder
            String fullPrompt = String.format(promptTemplate, userData);

            // 3. Build the Gemini API Request Payload
            ObjectNode root = mapper.createObjectNode();

            ObjectNode part = mapper.createObjectNode();
            part.put("text", fullPrompt);

            ObjectNode contentNode = mapper.createObjectNode();
            contentNode.putArray("parts").add(part);
            root.putArray("contents").add(contentNode);

            // THE FIX 1: Strict camelCase for responseMimeType
            ObjectNode config = mapper.createObjectNode();
            config.put("responseMimeType", "application/json");
            root.set("generationConfig", config);

            // 4. Send the Request
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(root), headers);
            // Splitting the string prevents IDEs and clipboards from auto-formatting it as a Markdown link
            String url = "https://generative" + "language.googleapis.com/v1beta/models/gemini-3.1-flash-lite:generateContent?key=" + apiKey;
            String responseStr = restTemplate.postForObject(url, request, String.class);

            // 5. Parse the Response
            JsonNode responseJson = mapper.readTree(responseStr);
            String extractedText = responseJson
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();

            return extractedText;

        } catch (Exception e) {
            e.printStackTrace();
            return """
                {
                  "identity_archetype": "SYSTEM ERROR",
                  "global_benchmark": "API Connection Failed.",
                  "extracted_skills": ["Debugging"],
                  "system_warnings": "Check the Spring Boot console for exact error logs.",
                  "the_marginal_edge": "Review OracleService.java payload configuration."
                }
                """;
        }
    }
}
package com.rituals.api.service;

import com.rituals.api.dto.TelemetryRequest;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class OracleService {

    public String generateNeuralProjection(TelemetryRequest stats) {

        // --- THE MASTER SYSTEM PROMPT ---
        // When you plug in Gemini or OpenAI, this is what you send them.
        String systemPrompt = """
            You are a brutalist, elite AI performance coach for software engineers. 
            Analyze the following user telemetry (Deep Work Hours, Focus Breaches, Task Completion Rate, and Category Breakdown).
            
            You must output your analysis as a strict JSON object with exactly these 5 keys:
            1. "identity_archetype": A short, intense title based on their highest-volume categories (e.g., "Enterprise Architect", "Algorithmic Grinder").
            2. "global_benchmark": A brutally honest comparison of their stats against elite developers.
            3. "extracted_skills": A JSON array of 3-4 professional skills they actually acquired based on their task logs.
            4. "system_warnings": A harsh warning if focus breaches are high or completion is low. If stats are perfect, praise their surgical execution.
            5. "the_marginal_edge": One highly specific, actionable micro-habit they can implement tomorrow to beat 90% of their peers.
            
            Do not include markdown formatting or extra text. Only return the raw JSON.
            """;

        /* * ==========================================
         * 🔌 LLM API INTEGRATION POINT
         * ==========================================
         * To go live, delete the Mock Engine below and uncomment this logic:
         * * RestTemplate restTemplate = new RestTemplate();
         * String llmResponse = restTemplate.postForObject(
         * "https://api.openai.com/v1/chat/completions", // Or Gemini URL
         * buildApiRequest(systemPrompt, stats),
         * String.class
         * );
         * return parseJsonFromLLM(llmResponse);
         * ==========================================
         */

        // --- MOCK ENGINE (For immediate UI Testing) ---
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();

            double hours = Double.parseDouble(stats.getTotalHours());
            boolean isGrinding = hours > 10;
            boolean isDistracted = stats.getTotalBreaches() > 5;

            // 1. Identity Archetype
            if (stats.getCategoryMinutes() != null && stats.getCategoryMinutes().containsKey("Java")) {
                root.put("identity_archetype", "Backend Systems Architect");
            } else {
                root.put("identity_archetype", isGrinding ? "High-Velocity SDE Candidate" : "Drifting Developer");
            }

            // 2. Global Benchmark
            if (isGrinding && !isDistracted) {
                root.put("global_benchmark", "Your volume places you in the top 5% of your cohort. Your focus integrity suggests elite-level digital hygiene.");
            } else if (isGrinding && isDistracted) {
                root.put("global_benchmark", "High output, but sloppy execution. You are competing with the top 20%, but your " + stats.getTotalBreaches() + " focus breaches are leaking mental energy.");
            } else {
                root.put("global_benchmark", "Current telemetry falls below competitive industry baselines. Output must increase to survive upcoming technical assessments.");
            }

            // 3. Extracted Skills
            var skillsArray = mapper.createArrayNode();
            skillsArray.add("Endurance Execution");
            skillsArray.add("Context Switching (Negative)");
            if (stats.getCategoryMinutes() != null) {
                stats.getCategoryMinutes().keySet().forEach(skillsArray::add);
            }
            root.set("extracted_skills", skillsArray);

            // 4. System Warnings
            if (isDistracted) {
                root.put("system_warnings", "WARNING: Dopamine baseline compromised. Tab-switching is destroying your deep work capacity. Lock down your browser tomorrow.");
            } else {
                root.put("system_warnings", "SYSTEM NOMINAL: Surgical focus maintained. Do not break the streak.");
            }

            // 5. The Marginal Edge
            root.put("the_marginal_edge", "Most developers write code and close the laptop. Spend the final 10 minutes of your next session documenting your database schemas and architecture decisions. That single habit separates coders from engineers.");

            return mapper.writeValueAsString(root);

        } catch (Exception e) {
            return "{}";
        }
    }
}
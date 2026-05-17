package com.rituals.api.controller;

import com.rituals.api.model.RoutineTemplate;
import com.rituals.api.repository.RoutineTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoutineTemplateController {

    private final RoutineTemplateRepository routineTemplateRepository;

    @PostMapping("/{userId}")
    public ResponseEntity<RoutineTemplate> saveTemplate(
            @PathVariable("userId") String userId,
            @RequestBody RoutineTemplate template) {

        template.setId(userId + "_master");
        template.setUserId(userId);

        RoutineTemplate savedTemplate = routineTemplateRepository.save(template);
        return ResponseEntity.ok(savedTemplate);
    }

   @GetMapping("/{userId}")
    public ResponseEntity<RoutineTemplate> getTemplate(@PathVariable("userId") String userId) {
        return routineTemplateRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

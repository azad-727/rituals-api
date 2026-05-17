package com.rituals.api.model;


import com.mongodb.client.model.Collation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="routine_templates")
public class RoutineTemplate {

    @Id
    private String id;
    private String userId;

    @Builder.Default
    private Map<String, List<Task>> weeklySchedule = new HashMap<>();
    @Builder.Default
    private boolean isActive=true;

}

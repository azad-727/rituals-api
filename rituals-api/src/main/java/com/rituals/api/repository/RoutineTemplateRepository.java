package com.rituals.api.repository;

import com.rituals.api.model.RoutineTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoutineTemplateRepository extends MongoRepository<RoutineTemplate,String> {

    Optional<RoutineTemplate> findByUserId(String userId);
}

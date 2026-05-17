package com.rituals.api.repository;

import com.rituals.api.model.DailyLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends MongoRepository<DailyLog, String> {
    Optional<DailyLog> findByUserIdAndLogData(String userId, LocalDate logData);

    List<DailyLog> findByUserIdOrderByLogDataDesc(String userId);

}

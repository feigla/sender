package ru.bogdsvn.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bogdsvn.store.entities.TaskEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query("""
        SELECT task.id
        FROM TaskEntity task
        WHERE task.processedAt IS NULL
        AND  (task.latestTryAt IS NULL OR task.latestTryAt <= :latestTryAtBound)
        ORDER BY task.createdAt
    """)
    List<Long> findAllNotProcessedIds(@Param("latestTryAtBound") Instant latestTryAtBound);

    @Modifying
    @Query("UPDATE TaskEntity task SET task.processedAt = :processedAt WHERE task.id = :id")
    void updateProcessedAt(@Param("id") Long id, @Param("processedAt") Instant processedAt);

    @Modifying
    @Query("UPDATE TaskEntity task SET task.latestTryAt = :inst WHERE task.id = :id")
    void updateLatestTryAt(@Param("id") Long id, @Param("inst") Instant latestTryAt);

    @Query("""
        SELECT task 
        FROM TaskEntity task 
        WHERE task.id = :id
        AND task.processedAt IS NULL
        AND (task.latestTryAt IS NULL OR task.latestTryAt <= :latestTryAtBound)
    """)
    Optional<TaskEntity> findNotProcessedTaskById(@Param("id") Long id, @Param("latestTryAtBound") Instant latestTryAtBound);
}

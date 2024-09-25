package ru.bogdsvn.store.daos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.bogdsvn.store.entities.TaskEntity;
import ru.bogdsvn.store.repositories.TaskRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class TaskDao {

    private final TaskRepository taskRepository;

    /**
     * if task was rejected, any worker can't execute that task 10 sec
     */
    private final Duration TASK_TIME_OUT = Duration.ofSeconds(10);

    @Transactional
    public TaskEntity save(TaskEntity task) {
        return taskRepository.save(task);
    }

    public List<Long> findAllNotProcessedIds() {

        Instant latestTryAtBound = Instant.now().minus(TASK_TIME_OUT);

        return taskRepository.findAllNotProcessedIds(latestTryAtBound);
    }

    @Transactional
    public void updateProcessedAt(Long taskId) {
        taskRepository.updateProcessedAt(taskId, Instant.now());
    }

    @Transactional
    public void updateLatestTryAt(Long taskId) {
        taskRepository.updateLatestTryAt(taskId, Instant.now());
    }

    public Optional<TaskEntity> findNotProcessedTaskById(Long taskId) {

        Instant latestTryAtBound = Instant.now().minus(TASK_TIME_OUT);

        return taskRepository.findNotProcessedTaskById(taskId, latestTryAtBound);
    }

}

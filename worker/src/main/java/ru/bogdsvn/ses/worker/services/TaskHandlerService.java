package ru.bogdsvn.ses.worker.services;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bogdsvn.store.daos.TaskDao;
import ru.bogdsvn.store.entities.TaskEntity;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskHandlerService {

    private final TaskDao taskDao;

    private final EmailClientService emailClientService;
    private final LockWrapperService lockWrapperService;

    @Value("${rabbit.exchanger}")
    public String exchanger;

    private final static String TASK_KEY_FORMATTED = "bogdsvn:sender:task:%s";

    @RabbitListener(queues = "q1")
    public void handleTask(Message message, Channel channel)  {
        long id = Long.parseLong(new String(message.getBody(), StandardCharsets.UTF_8));
        log.info("I'm getting task " + id);
        lockWrapperService.lockOrExecute(
                id,
                getKey(id),
                Duration.ofSeconds(5),
                () -> sendEmail(id)
        );
    }

    private void sendEmail(long id) {
        TaskEntity task = taskDao.findNotProcessedTaskById(id).orElse(null);
        if (task == null) {
            log.info("Task is already processed " + id);
            return;
        }
        log.info("Task is processed " + task.getId());
        boolean isReceived = emailClientService.sendEmail(task.getDestinationEmail(), task.getMessage());
        if (isReceived) {
            taskDao.updateProcessedAt(task.getId());
            log.info("Task is completed " + task.getId());
        } else {
            log.info("Task is rejected " + task.getId());
            taskDao.updateLatestTryAt(task.getId());
        }
    }

    public static String getKey(long id) {
        return TASK_KEY_FORMATTED.formatted(id);
    }
}

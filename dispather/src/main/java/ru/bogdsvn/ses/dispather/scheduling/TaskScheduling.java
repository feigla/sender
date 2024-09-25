package ru.bogdsvn.ses.dispather.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bogdsvn.store.daos.TaskDao;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskScheduling {

    private final TaskDao taskDao;


    @Value("${rabbit.exchanger}")
    public String exchanger;

    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "*/5 * * * * *")
    public void executeSendEmail() {
        List<Long> ids = taskDao.findAllNotProcessedIds();

        for (Long id : ids) {
            rabbitTemplate.convertAndSend(exchanger, null, Long.toString(id));
        }
    }


}

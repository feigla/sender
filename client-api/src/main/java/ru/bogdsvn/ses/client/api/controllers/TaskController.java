package ru.bogdsvn.ses.client.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.bogdsvn.store.daos.TaskDao;
import ru.bogdsvn.store.entities.TaskEntity;

@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskDao taskDao;

    private static final String SEND_EMAIL = "api/email/send";

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(SEND_EMAIL)
    public void sendEmail(@RequestParam("destination_email") String destinationEmail,
                          @RequestParam("message") String message) {
        taskDao.save(TaskEntity.builder()
                        .destinationEmail(destinationEmail)
                        .message(message)
                        .build());
    }
}

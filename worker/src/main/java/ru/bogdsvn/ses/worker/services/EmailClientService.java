package ru.bogdsvn.ses.worker.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailClientService {

    Random random = new Random();

    /**
     * @return true if message is sent otherwise return false
     */
    public boolean sendEmail(String destinationEmail, String message) {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            return false;
        }
        return random.nextInt(20) < 10; // 50% that's true

    }
}

package ru.bogdsvn.ses.worker.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.bogdsvn.ses.reddis.services.RedisLockService;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class LockWrapperService {

    private final RedisLockService redisLockService;

    public void lockOrExecute(Long id, String key, Duration duration, Runnable runnable) {
        if (redisLockService.acquireLock(key, duration)) {
            runnable.run();
            redisLockService.releaseLock(key);
        } else {
            log.info("Task is blocked " + id);
        }

    }
}

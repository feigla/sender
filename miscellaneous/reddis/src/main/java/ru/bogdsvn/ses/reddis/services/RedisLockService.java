package ru.bogdsvn.ses.reddis.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class RedisLockService {

    private static final String LOCK_FORMAT = "%s:lock";

    private final ValueOperations<String, Long> ops;
    private final RedisTemplate<String, Long> redisTemplate;

    public RedisLockService(@Qualifier("valueOperations") ValueOperations<String, Long> ops, RedisTemplate<String, Long> redisTemplate) {
        this.ops = ops;
        this.redisTemplate = redisTemplate;
    }

    public boolean acquireLock(String key, Duration duration) {
        String lockKey = getLockKey(key);

        Long expiredAtMillis = ops.get(lockKey);

        long currentTimeMillis = System.currentTimeMillis();

        if (Objects.nonNull(expiredAtMillis)) {
            if (currentTimeMillis <= expiredAtMillis) {
                return false;
            }
            redisTemplate.delete(lockKey);
        }

        return Optional
                    .ofNullable(
                        ops.setIfAbsent(
                                lockKey,
                                currentTimeMillis + duration.toMillis()
                        )).orElse(false);

    }

    public void releaseLock(String key) {
        String lockKey = getLockKey(key);

        redisTemplate.delete(lockKey);
    }


    private static String getLockKey(String key) {
        return LOCK_FORMAT.format(key);
    }

}

package com.intuit.craft.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
@Slf4j
@AllArgsConstructor
public class RedisLockService {

    private static final long DEFAULT_EXPIRE_UNUSED = 600L;

    private final RedisLockRegistry redisLockRegistry;

    public boolean tryLock(String lockKey, long seconds) {
        Lock lock = obtainLock(lockKey);
        try {
            log.info("After getting lock object");
            return lock.tryLock(seconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void unlock(String lockKey) {
        try {
            Lock lock = obtainLock(lockKey);
            if (lock != null) {
                lock.unlock();
            }
            redisLockRegistry.expireUnusedOlderThan(DEFAULT_EXPIRE_UNUSED);
        } catch (Exception e) {
            throw e;
        }
    }

    private Lock obtainLock(String lockKey) {
        return redisLockRegistry.obtain(lockKey);
    }

    public void releaseFirstLevelLocks(List<String> lockKeys) {
        lockKeys.forEach(this::unlock);
    }
}

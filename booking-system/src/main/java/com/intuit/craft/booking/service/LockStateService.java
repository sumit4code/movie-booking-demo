package com.intuit.craft.booking.service;

import com.intuit.craft.booking.domain.LockState;
import com.intuit.craft.booking.domain.LockStatus;
import com.intuit.craft.booking.domain.LockType;
import com.intuit.craft.booking.reposiotry.LockStateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
public class LockStateService {

    private final long lockDuration;
    private final LockStateRepository lockStateRepository;
    private final RedisLockService redisLockService;

    public LockStateService(@Value("${booking.locking.time-in-seconds}") long lockDuration,
                            LockStateRepository lockStateRepository, RedisLockService redisLockService) {
        this.lockDuration = lockDuration;
        this.lockStateRepository = lockStateRepository;
        this.redisLockService = redisLockService;
    }

    public List<LockState> saveAll(List<LockState> lockStates) {
        return lockStateRepository.saveAll(lockStates);
    }

    public LockState update(LockState lockState) {
        return lockStateRepository.save(lockState);
    }

    public void releaseUnProcessedLock(Pageable pageable) {
        Page<LockState> all1 = lockStateRepository.findAll(pageable);
        while (!all1.isEmpty()) {
            List<LockState> lockStates = all1.getContent();
            lockStates.forEach(this::releaseUnProcessedLock);
            pageable = all1.nextPageable();
            all1 = lockStateRepository.findAll(pageable);
        }
    }

    public void releaseUnProcessedLock(LockState lockState) {
        log.debug("Request received for releasing unprocessed locks {}", lockState);
        if (lockState.getLockstatus() == LockStatus.Acquired || lockState.getLockstatus() == LockStatus.Initiated) {
            long configuredExpiryTime = (lockState.getLockType() == LockType.FIRST_LEVEL ? 300 : lockDuration) * 1000;
            if (lockState.getLastModifiedDate().toInstant(ZoneOffset.UTC).plusMillis(configuredExpiryTime).isAfter(Instant.now())) {
                redisLockService.unlock(lockState.getLockId());
                lockState.setLockstatus(LockStatus.Released);
                this.update(lockState);
                log.info("Successfully released lock {}", lockState.getLockId());
            }
        }
    }

    public void releaseLocks(List<String> lockKeys) {
        lockKeys.forEach(redisLockService::unlock);
    }

}

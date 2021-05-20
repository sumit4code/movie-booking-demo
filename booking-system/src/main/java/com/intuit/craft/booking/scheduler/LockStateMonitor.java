package com.intuit.craft.booking.scheduler;

import com.intuit.craft.booking.service.LockStateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@EnableScheduling
@Component
public class LockStateMonitor {

    private final LockStateService lockStateService;

    @Scheduled(cron = "*/10 * * * * *")
    public void releasedUnProcessedLocks(){
        log.info("Scheduler service started");
        Pageable pageRequest = PageRequest.of(0, 100);

        lockStateService.releaseUnProcessedLock(pageRequest);
        log.info("Scheduler service end");
    }

}

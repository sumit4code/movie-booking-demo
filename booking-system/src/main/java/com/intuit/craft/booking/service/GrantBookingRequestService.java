package com.intuit.craft.booking.service;

import com.intuit.craft.booking.domain.BookingRequest;
import com.intuit.craft.booking.domain.EventSeatMapper;
import com.intuit.craft.booking.domain.LockState;
import com.intuit.craft.booking.domain.LockStatus;
import com.intuit.craft.booking.domain.LockType;
import com.intuit.craft.booking.domain.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.intuit.craft.booking.domain.Status.Booked;

@Service
@Slf4j
public class GrantBookingRequestService {

    public static final String LOCK_KEY_FORMAT = "%s-%s-%s-%s";
    public static final int FIRST_LEVEL_LOCK_DURATION_IN_SECONDS = 2;

    private final BookingEventService bookingEventService;
    private final RedisLockService redisLockService;
    private final LockStateService lockStateService;
    private final long lockDuration;

    @Autowired
    public GrantBookingRequestService(BookingEventService bookingEventService, RedisLockService redisLockService,
                                      LockStateService lockStateService, @Value("${booking.locking.time-in-seconds}") Long lockDuration) {
        this.bookingEventService = bookingEventService;
        this.redisLockService = redisLockService;
        this.lockStateService = lockStateService;
        this.lockDuration = lockDuration;
    }

    public boolean grant(BookingRequest bookingRequest) {
        boolean result = false;
        List<String> firstLevelLockKeys = getLockKeys(bookingRequest, LockType.FIRST_LEVEL);
        boolean firstLockGranted = grant(firstLevelLockKeys, FIRST_LEVEL_LOCK_DURATION_IN_SECONDS);
        if (firstLockGranted) {
            List<String> secondLevelLockKeys = getLockKeys(bookingRequest, LockType.SECOND_LEVEL);
            List<EventSeatMapper> requestedSeat = this.bookingEventService.retrieveRequestedSeat(bookingRequest);
            if (requestedSeat.stream().noneMatch(eventSeatMapper -> eventSeatMapper.getStatus() == Booked)) {
                log.info("Requested seats are available to proceed");
                List<LockState> initiatedLockState = initSecondLevelLock(secondLevelLockKeys);
                if (grant(secondLevelLockKeys, lockDuration)) {
                    log.debug("Acquired log");
                    updateSecondLevelLockAsAcquired(initiatedLockState);
                    publishForPartialBooking(requestedSeat);
                    result = true;
                }
            }
            lockStateService.releaseLocks(firstLevelLockKeys);
        }
        return result;
    }

    private void updateSecondLevelLockAsAcquired(List<LockState> initiatedLockState) {
        List<LockState> acquiredLocks = initiatedLockState.stream().peek(lockState -> lockState.setLockstatus(LockStatus.Acquired)).collect(Collectors.toList());
        lockStateService.saveAll(acquiredLocks);
    }

    private List<LockState> initSecondLevelLock(List<String> secondLevelLockKeys) {
        List<LockState> lockStates = secondLevelLockKeys.stream().map(s -> LockState.builder().lockId(s).lockType(LockType.SECOND_LEVEL).lockstatus(LockStatus.Initiated).build()).collect(Collectors.toList());
        return lockStateService.saveAll(lockStates);
    }

    private boolean grant(List<String> lockKeys, long lockDuration) {
        //ToDo verify free seat count is grater than the requested count
        boolean result = false;
        List<String> processedLockingKeys = new ArrayList<>(lockKeys.size());
        for (String lockKey : lockKeys) {
            log.info("Locking request came for key {}", lockKey);
            if (redisLockService.tryLock(lockKey, lockDuration)) {
                result = true;
                processedLockingKeys.add(lockKey);
            } else {
                result = false;
                break;
            }
        }
        if (!result) {
            if (!processedLockingKeys.isEmpty()) {
                log.info("Releasing lock for seats {}", processedLockingKeys);
                processedLockingKeys.forEach(redisLockService::unlock);
            }
            return false;
        }
        log.info("process completed lock for seats {}", processedLockingKeys);
        return true;
    }

    private void publishForPartialBooking(List<EventSeatMapper> requestedSeat) {
        List<EventSeatMapper> lockedSeats = requestedSeat.stream().peek(eventSeatMapper -> eventSeatMapper.setStatus(Status.Locked)).collect(Collectors.toList());
        bookingEventService.updateSeatStatus(lockedSeats);
        log.info("Total lock seat count {}", lockedSeats.size());
    }

    private List<String> getLockKeys(BookingRequest bookingRequest, LockType lockType) {
        return bookingRequest.getSeatNumbers().stream()
                .map(seat -> String.format(LOCK_KEY_FORMAT, bookingRequest.getEventId(), seat.getRowNumber(), seat.getSeatNumber(), lockType.name()))
                .collect(Collectors.toList());
    }

}

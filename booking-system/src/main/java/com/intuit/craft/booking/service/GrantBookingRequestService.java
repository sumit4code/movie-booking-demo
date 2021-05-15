package com.intuit.craft.booking.service;

import com.intuit.craft.booking.domain.BookingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GrantBookingRequestService {

    public static final String LOCK_KEY_FORMAT = "%s-%s-%s";

    private final RedisLockService redisLockService;
    private final long lockDuration;

    @Autowired
    public GrantBookingRequestService(RedisLockService redisLockService,
                                      @Value("${booking.locking.time-in-seconds}") Long lockDuration) {
        this.redisLockService = redisLockService;
        this.lockDuration = lockDuration;
    }

    /**
     * Acquires lock in each of the seat, if any of seat is lock of some other use, it releases locks the other seats
     *
     * @param bookingRequest
     * @return
     */
    public boolean grant(BookingRequest bookingRequest) {
        //ToDo verify free seat count is grater than the requested count
        boolean result = false;
        List<String> lockKeys = bookingRequest.getSeatNumbers().stream()
                .map(seat -> String.format(LOCK_KEY_FORMAT, bookingRequest.getEventId(), seat.getRowNumber(), seat.getSeatNumber()))
                .collect(Collectors.toList());
        List<String> processedLockingKeys = new ArrayList<>(bookingRequest.getSeatNumbers().size());
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
            if(!processedLockingKeys.isEmpty()){
                log.info("Releasing lock for seats {}", processedLockingKeys);
                processedLockingKeys.forEach(redisLockService::unlock);
            }
        }
        log.info("process completed lock for seats {}", processedLockingKeys);
        publishForPartialBooking(bookingRequest);
        return result;
    }

    //ToDo kafka publishing
    private void publishForPartialBooking(BookingRequest bookingRequest) {
        //will do
    }
}

package com.intuit.craft.booking.service;

import com.intuit.craft.booking.domain.Booking;
import com.intuit.craft.booking.domain.BookingLockRequest;
import com.intuit.craft.booking.domain.BookingRequest;
import com.intuit.craft.booking.domain.EventSeatMapper;
import com.intuit.craft.booking.domain.EventTask;
import com.intuit.craft.booking.domain.Seat;
import com.intuit.craft.booking.domain.Status;
import com.intuit.craft.booking.domain.TransactionStatus;
import com.intuit.craft.booking.reposiotry.EventSeatMapperRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class BookingEventService {

    private final EventSeatMapperRepository eventSeatMapperRepository;
    private final BookingRepository bookingRepository;
    private final LockStateService lockStateService;

    public void allocateSeatsForEvent(EventTask eventTask) {
        List<EventSeatMapper> eventSeatMapperList = eventTask.getScreen().getLayout().getSeats().stream()
                .map(this::getNumericSeatValue)
                .map(integer -> EventSeatMapper.builder().eventId(eventTask.getEventId())
                        .subEventId(eventTask.getSubEvent().getSubEventId())
                        .seatNumber(integer)
                        .status(Status.Available).build())
                .collect(Collectors.toList());
        eventSeatMapperRepository.saveAll(eventSeatMapperList);
        log.info("Successfully allocated total seats {} for event {}", eventSeatMapperList.size(), eventTask.getEventId());
    }

    public Optional<List<EventSeatMapper>> retrieveAllSeats(String eventId, String subEventId) {
        log.info("Request Received for retrieving all seats for event {} & sub event {}", eventId, subEventId);
        return eventSeatMapperRepository.findByEventIdAndSubEventId(eventId, subEventId);
    }

    public List<EventSeatMapper> retrieveRequestedSeat(BookingLockRequest bookingLockRequest) {
        List<Integer> seatList = bookingLockRequest.getSeatNumbers().stream().map(this::getNumericSeatValue).collect(Collectors.toList());
        Optional<List<EventSeatMapper>> eventSeatMapperList = this.retrieveAllSeats(bookingLockRequest.getEventId(), bookingLockRequest.getSubEventId());
        if (eventSeatMapperList.isPresent()) {
            List<EventSeatMapper> filteredSeat = eventSeatMapperList.get().stream().filter(eventSeatMapper -> matchSeat(seatList, eventSeatMapper.getSeatNumber())).collect(Collectors.toList());
            return filteredSeat.size() == seatList.size() ? filteredSeat : Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public List<EventSeatMapper> updateSeatStatus(List<EventSeatMapper> updatedSeat) {
        return eventSeatMapperRepository.saveAll(updatedSeat);
    }

    public boolean matchSeat(List<Integer> seats, int seatNumber) {
        return seats.contains(seatNumber);
    }

    private int getNumericSeatValue(Seat seat) {
        return Integer.parseInt(seat.getRowNumber() + "" + seat.getSeatNumber());
    }

    public boolean booking(BookingRequest bookingRequest) {
        log.info("Triggering transaction request for {}", bookingRequest.getTransactionId());
        TransactionStatus status = performTransaction(bookingRequest);
        bookingRepository.save(Booking.builder().transactionId(bookingRequest.getTransactionId())
                .amount(bookingRequest.getAmount()).emailId(bookingRequest.getEmailId())
                .mobileNumber(bookingRequest.getMobileNumber()).seats(bookingRequest.getSeats())
                .status(status).build());
        log.info("booking is completed with status {}", status);
        if (status == TransactionStatus.Success) {
            updateSeatStatus(bookingRequest);
            lockStateService.releaseSecondLevelLockOnSuccessfulTransaction(bookingRequest.getTransactionId());
        }
        return status == TransactionStatus.Success;
    }

    private void updateSeatStatus(BookingRequest bookingRequest) {
        List<Integer> lockedSeat = bookingRequest.getSeats().stream().map(this::getNumericSeatValue).collect(Collectors.toList());
        List<EventSeatMapper> bookedSeat = eventSeatMapperRepository.findByEventIdAndSubEventId(bookingRequest.getEventId(), bookingRequest.getSubEventId())
                .get().stream().filter(eventSeatMapper -> lockedSeat.contains(eventSeatMapper.getSeatNumber())).peek(eventSeatMapper -> eventSeatMapper.setStatus(Status.Booked))
                .collect(Collectors.toList());
        eventSeatMapperRepository.saveAll(bookedSeat);
    }

    private TransactionStatus performTransaction(BookingRequest bookingRequest) {
        if (bookingRequest.getAmount().compareTo(BigDecimal.valueOf(30)) > 0) {
            return TransactionStatus.Failure;
        }
        return TransactionStatus.Success;
    }


    public Optional<List<Booking>> findAll() {
        return Optional.of(bookingRepository.findAll());
    }

}

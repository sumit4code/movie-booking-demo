package com.intuit.craft.booking.service;

import com.intuit.craft.booking.domain.BookingRequest;
import com.intuit.craft.booking.domain.EventSeatMapper;
import com.intuit.craft.booking.domain.EventTask;
import com.intuit.craft.booking.domain.Seat;
import com.intuit.craft.booking.domain.Status;
import com.intuit.craft.booking.reposiotry.EventSeatMapperRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class BookingEventService {

    private final EventSeatMapperRepository eventSeatMapperRepository;

    public void allocateSeatsForEvent(EventTask eventTask) {
        List<EventSeatMapper> eventSeatMapperList = eventTask.getScreen().getLayout().getSeats().stream()
                .map(this::getNumericSeatValue)
                .map(integer -> EventSeatMapper.builder().eventId(eventTask.getEventId()).subEventId(eventTask.getSubEvent().getSubEventId()).status(Status.Available).build())
                .collect(Collectors.toList());
        eventSeatMapperRepository.saveAll(eventSeatMapperList);
        log.info("Successfully allocated total seats {} for event {}", eventSeatMapperList.size(), eventTask.getEventId());
    }

    public Optional<List<EventSeatMapper>> retrieveAllSeats(String eventId, String subEventId) {
        log.info("Request Received for retrieving all seats for event {} & sub event {}", eventId, subEventId);
        return eventSeatMapperRepository.findByEventIdAndSubEventId(eventId, subEventId);
    }

    public List<EventSeatMapper> retrieveRequestedSeat(BookingRequest bookingRequest) {
        List<Integer> seatList = bookingRequest.getSeatNumbers().stream().map(this::getNumericSeatValue).collect(Collectors.toList());
        Optional<List<EventSeatMapper>> eventSeatMapperList = this.retrieveAllSeats(bookingRequest.getEventId(), bookingRequest.getSubEventId());
        if (eventSeatMapperList.isPresent()) {
            List<EventSeatMapper> eventSeatMappers = eventSeatMapperList.get();
            List<EventSeatMapper> filteredSeat = eventSeatMappers.stream().filter(eventSeatMapper -> seatList.contains(eventSeatMapper.getSeatNumber())).collect(Collectors.toList());
            return filteredSeat.size() == seatList.size() ? filteredSeat : Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public List<EventSeatMapper> updateSeatStatus(List<EventSeatMapper> updatedSeat) {
        return eventSeatMapperRepository.saveAll(updatedSeat);
    }

    private int getNumericSeatValue(Seat seat) {
        return Integer.parseInt(seat.getRowNumber() + "" + seat.getSeatNumber());
    }
}

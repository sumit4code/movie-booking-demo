package com.intuit.craft.theater.service;

import com.intuit.craft.exception.ValidationException;
import com.intuit.craft.theater.domain.Event;
import com.intuit.craft.theater.domain.EventDetail;
import com.intuit.craft.theater.domain.Theater;
import com.intuit.craft.theater.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.intuit.craft.theater.helper.DateWiseEventHelper.createSubEvent;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

    private final LocationService locationService;
    private final TheaterService theaterService;
    private final EventRepository eventRepository;
    private final PublishingService publishingService;

    public Event process(Event event) {
        log.info("Creating event");
        event.validate();
        validateLocation(event);
        Theater theater = retrieveTheaterInformation(event);
        enrichSubEventList(event);
        Event savedEvent = eventRepository.save(event);
        log.info("Event has been created successfully");
        EventDetail eventDetail = EventDetail.from(savedEvent, theater);
        publishingService.publish(eventDetail);
        return savedEvent;
    }

    private void enrichSubEventList(Event event) {
        event.setSubEvents(createSubEvent(event.getEventStartDate(), event.getEventStartDate(), event.getSchedules()));
    }

    private Theater retrieveTheaterInformation(Event event) {
        Theater theater = theaterService.get(event.getTheaterId());
        if (theater == null) {
            throw new ValidationException("Invalid theater has been provided");
        }
        if (theater.getScreen().stream().noneMatch(screen -> screen.getId() == event.getScreenId())) {
            throw new ValidationException("Invalid screen has been provided");
        }
        return theater;
    }

    private void validateLocation(Event event) {
        boolean validLocation = locationService.getCities().stream()
                .anyMatch(locationInformation -> locationInformation.getId().equalsIgnoreCase(event.getLocationId()));
        if (!validLocation) {
            throw new ValidationException("Invalid location has been provided");
        }
    }
}

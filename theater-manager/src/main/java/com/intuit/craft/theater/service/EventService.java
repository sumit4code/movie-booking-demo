package com.intuit.craft.theater.service;

import com.intuit.craft.exception.ValidationException;
import com.intuit.craft.kafka.EventMessage;
import com.intuit.craft.kafka.MessageHeader;
import com.intuit.craft.kafka.service.MessagePublisher;
import com.intuit.craft.theater.domain.Event;
import com.intuit.craft.theater.domain.EventDetail;
import com.intuit.craft.theater.domain.Theater;
import com.intuit.craft.theater.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.intuit.craft.theater.helper.DateWiseEventHelper.createSubEvent;

@Slf4j
@Service
public class EventService {

    private final String destinationTopic;
    private final LocationService locationService;
    private final TheaterService theaterService;
    private final EventRepository eventRepository;
    private final MessagePublisher messagePublisher;

    @Autowired
    public EventService(@Value("${kafka.message.destination.topic}") String destinationTopic,
                        LocationService locationService, TheaterService theaterService,
                        EventRepository eventRepository, MessagePublisher messagePublisher) {
        this.destinationTopic = destinationTopic;
        this.locationService = locationService;
        this.theaterService = theaterService;
        this.eventRepository = eventRepository;
        this.messagePublisher = messagePublisher;
    }

    public Event process(Event event) {
        log.info("Creating event");
        event.validate();
        validateLocation(event);
        Theater theater = retrieveTheaterInformation(event);
        enrichSubEventList(event);
        Event savedEvent = eventRepository.save(event);
        log.info("Event has been created successfully");
        EventDetail eventDetail = EventDetail.from(savedEvent, theater);
        publishEvent(savedEvent, eventDetail);
        return savedEvent;
    }

    private void publishEvent(Event savedEvent, EventDetail eventDetail) {
        MessageHeader messageHeader = MessageHeader.builder().topic(destinationTopic).messageId(savedEvent.getId()).build();
        EventMessage eventMessage = new EventMessage(messageHeader, eventDetail);
        messagePublisher.publish(eventMessage);
    }

    private void enrichSubEventList(Event event) {
        event.setSubEvents(createSubEvent(event.getEventStartDate(), event.getEventEndDate(), event.getSchedules()));
    }

    private Theater retrieveTheaterInformation(Event event) {
        Theater theater = theaterService.get(event.getTheaterId());
        if (theater == null) {
            throw new ValidationException("Invalid theater has been provided");
        }
        if (theater.getScreen().stream().noneMatch(screen -> screen.getId() == event.getScreenId())) {
            throw new ValidationException("Invalid screen has been provided");
        }
        log.debug("Successfully retrieved all the information from theater id {}", event.getTheaterId());
        return theater;
    }

    private void validateLocation(Event event) {
        boolean validLocation = locationService.getCities().stream()
                .anyMatch(locationInformation -> locationInformation.getId().equalsIgnoreCase(event.getLocationId()));
        if (!validLocation) {
            throw new ValidationException("Invalid location has been provided");
        }
    }

    public Optional<List<Event>> retrieveEvents(String locationId, String eventId) {
        log.debug("Retrieving events based on location {} and eventId {}", locationId, eventId);
        return eventRepository.findByLocationIdAndEventId(locationId, eventId);
    }

    public Optional<Optional<Event>> retrieveEvent(String id) {
        log.debug("Retrieving specific event {}", id);
        return Optional.of(eventRepository.findById(id));
    }
}

package com.intuit.craft.booking.consumer;

import com.intuit.craft.booking.domain.EventDetail;
import com.intuit.craft.booking.domain.EventTask;
import com.intuit.craft.booking.service.PublishingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Receives instruction from kafka for booking event and then divide into multiple
 * smaller task with {@link EventTask} and publish back to kafka and distribute the load
 */
@Slf4j
@AllArgsConstructor
@Component
public class BookingEventAllocationConsumer {

    private final PublishingService publishingService;

    public void processBookingEvent(@Payload EventDetail eventDetail) {
        log.info("Received new event with id {}", eventDetail.getEventId());
        eventDetail.getSubEvents().forEach(subEvent -> {
            EventTask eventTask = EventTask.builder().eventId(eventDetail.getEventId())
                    .screen(eventDetail.getScreen())
                    .subEvent(subEvent).build();
            publishingService.publish(eventTask);
            log.info("Submitted sub task {} for event : {}", eventTask.getSubEvent().getSubEventId(), eventTask.getEventId());
        });
    }
}

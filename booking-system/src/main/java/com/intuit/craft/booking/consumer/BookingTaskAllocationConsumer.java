package com.intuit.craft.booking.consumer;

import com.intuit.craft.booking.domain.EventTask;
import com.intuit.craft.booking.service.BookingEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Receives messages from kafka as form of {@link EventTask}
 * and allocates seats for particular booking event for particular time
 */
@Slf4j
@Component
@AllArgsConstructor
public class BookingTaskAllocationConsumer {

    private final BookingEventService bookingEventService;

    @KafkaListener(id = "event-task-consumer", topics = "${kafka.consumer.topic.booking-task-creation}", groupId = "${spring.application.name}")
    public void processBookingTask(@Payload EventTask eventTask) {
        log.info("Received new task with id {}", eventTask.getSubEvent().getSubEventId());

        bookingEventService.allocateSeatsForEvent(eventTask);
        log.info("Completed task new event");
    }
}

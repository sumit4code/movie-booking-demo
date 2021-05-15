package com.intuit.craft.booking.consumer;

import com.intuit.craft.booking.domain.EventDetail;
import com.intuit.craft.booking.domain.EventTask;
import com.intuit.craft.kafka.EventMessage;
import com.intuit.craft.kafka.MessageHeader;
import com.intuit.craft.kafka.service.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Receives instruction from kafka for booking event and then divide into multiple
 * smaller task with {@link EventTask} and publish back to kafka and distribute the load
 */
@Slf4j
@Component
public class BookingEventAllocationConsumer {

    private final String destinationTopic;
    private final MessagePublisher messagePublisher;

    @Autowired
    public BookingEventAllocationConsumer(@Value("${kafka.producer.topic.booking-task-creation}") String destinationTopic, MessagePublisher messagePublisher) {
        this.destinationTopic = destinationTopic;
        this.messagePublisher = messagePublisher;
    }

    @KafkaListener(id = "event-message-consumer", topics = "${kafka.consumer.topic.booking-event-creation}", groupId = "${spring.application.name}")
    public void processBookingEvent(@Payload EventDetail eventDetail) {
        log.info("Received new event with id {}", eventDetail.getEventId());
        eventDetail.getSubEvents().forEach(subEvent -> {
            EventTask eventTask = EventTask.builder().eventId(eventDetail.getEventId())
                    .screen(eventDetail.getScreen())
                    .subEvent(subEvent).build();

            publishEvent(eventTask, eventDetail);
            log.info("Submitted sub task {} for event : {}", eventTask.getSubEvent().getSubEventId(), eventTask.getEventId());
        });
    }

    private void publishEvent(EventTask eventTask, EventDetail eventDetail) {
        MessageHeader messageHeader = MessageHeader.builder().topic(destinationTopic).messageId(eventDetail.getEventId()).build();
        EventMessage eventMessage = new EventMessage(messageHeader, eventTask);
        messagePublisher.publish(eventMessage);
    }
}

package com.intuit.craft.kafka.service;

import com.intuit.craft.Converter;
import com.intuit.craft.kafka.EventMessage;
import com.intuit.craft.kafka.MessageHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class MessagePublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Converter converter;

    public MessagePublisher(@Autowired(required = false) KafkaTemplate<String, String> kafkaTemplate, Converter converter) {
        this.kafkaTemplate = kafkaTemplate;
        this.converter = converter;
    }

    public ListenableFuture<SendResult<String, String>> publish(@NotNull EventMessage eventMessage) {
        String stringMessage = converter.convert(eventMessage.getPayload());
        MessageHeader header = eventMessage.getHeader();

        ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(header.getTopic(), header.getMessageId(), stringMessage);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message: " + header.getMessageId() + " with offset: " + result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message : " + header.getMessageId(), ex);
            }
        });
        return future;
    }
}

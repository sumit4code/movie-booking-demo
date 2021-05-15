package com.intuit.craft.theater.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PublishingService {

    //ToDO kafka send for to creates seats for event
    public void publish(Object object) {
        //do Nothing
        log.info("Publishing event  {}", object);
    }
}

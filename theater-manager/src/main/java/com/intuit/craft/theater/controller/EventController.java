package com.intuit.craft.theater.controller;

import com.intuit.craft.theater.domain.Event;
import com.intuit.craft.theater.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Validated
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/api/v1/event")
@Api(value = "CRUD operation on event", tags = {"API :: Movie Event"})
public class EventController {

    private EventService eventService;

    @PostMapping
    @ApiOperation(value = "Creating new Event")
    public ResponseEntity<Object> createEvent(@Valid @RequestBody Event event){
        Event processedEvent = eventService.process(event);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(processedEvent.getEventId()).toUri();
        return ResponseEntity.created(location).build();
    }

}

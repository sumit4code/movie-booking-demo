package com.intuit.craft.theater.controller;

import com.intuit.craft.theater.domain.Event;
import com.intuit.craft.theater.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/api/v1/event")
@Api(value = "CRUD operation on event", tags = {"API :: Movie Event"})
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ApiOperation(value = "Creating new Event specific to theater")
    public ResponseEntity<Object> createEvent(@Valid @RequestBody Event event) {
        Event processedEvent = eventService.process(event);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(processedEvent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Fetch event specific to theater")
    public ResponseEntity<Optional<Event>> retrieveEvent(@PathVariable(value = "id") String id) {
        return ResponseEntity.of(eventService.retrieveEvent(id));
    }

    @GetMapping
    @ApiOperation(value = "Fetch events specific to location")
    public ResponseEntity<List<Event>> retrieveEvent(@RequestParam String eventId, @RequestParam String locationId) {
        return ResponseEntity.of(eventService.retrieveEvents(locationId, eventId));
    }
}

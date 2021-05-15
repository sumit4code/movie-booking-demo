package com.intuit.craft.theater.controller;

import com.intuit.craft.theater.domain.Theater;
import com.intuit.craft.theater.service.TheaterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping(value = "/api/v1/theater")
@Api(value = "CRUD operation on theater", tags = {"API :: Theater"})
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping
    @ApiOperation(value = "create a new theater")
    public ResponseEntity<Theater> create(@Valid @RequestBody Theater theater) {
        Theater savedTheater = theaterService.create(theater);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedTheater.getId()).toUri();
        return ResponseEntity.created(location).body(savedTheater);
    }

    @PutMapping
    @ApiOperation(value = "update a theater")
    public ResponseEntity<Theater> update(@PathVariable(value = "id") String id, @Valid @RequestBody Theater theater) {
        Theater updatedTheater = theaterService.update(id, theater);
        return ResponseEntity.ok(updatedTheater);
    }

    @GetMapping
    @ApiOperation(value = "retrieve a theater based on id")
    public ResponseEntity<Theater> get(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(theaterService.get(id));
    }
}

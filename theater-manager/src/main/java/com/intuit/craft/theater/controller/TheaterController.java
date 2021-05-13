package com.intuit.craft.theater.controller;

import com.intuit.craft.theater.domain.Theater;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@Slf4j
@AllArgsConstructor
public class TheaterController {


    public ResponseEntity<Theater> create(@Valid @RequestBody Theater theater) {
        return ResponseEntity.created(null).body(null);
    }

    public ResponseEntity<Theater> update(@PathVariable String id, @Valid @RequestBody Theater theater) {
        return ResponseEntity.created(null).body(null);
    }

    public ResponseEntity<Theater> get(String id) {
        return ResponseEntity.ok(null);
    }


}

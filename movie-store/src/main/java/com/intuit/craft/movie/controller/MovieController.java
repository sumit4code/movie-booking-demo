package com.intuit.craft.movie.controller;

import com.intuit.craft.movie.domain.Movie;
import com.intuit.craft.movie.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/movie")
@Validated
@Api(value = "CRUD operation on movie", tags = {"API :: Movie"})
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @ApiOperation(value = "Validate and store movie record")
    public ResponseEntity<Movie> create(@Valid @RequestBody Movie movie) {
        log.debug("Received request for creating Movie {}", movie);
        Movie saved = movieService.create(movie);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation(value = "Validate and update movie record")
    public ResponseEntity<Movie> update(@Valid @RequestBody Movie movie) {
        log.debug("Received request for updating movie {}", movie);
        Movie saved = movieService.update(movie);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Retrieve movie record")
    public ResponseEntity<Movie> get(@PathVariable(value = "id") String id) {
        log.debug("Received request for getting movie movie {}", id);
        Movie movie = movieService.get(id);
        return ResponseEntity.ok(movie);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete movie record")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        log.debug("Received request for deleting movie {}", id);
        movieService.delete(id);
        return ResponseEntity.ok().build();
    }
}

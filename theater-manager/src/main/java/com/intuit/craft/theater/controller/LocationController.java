package com.intuit.craft.theater.controller;

import com.intuit.craft.theater.domain.LocationInformation;
import com.intuit.craft.theater.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/location")
@Slf4j
@AllArgsConstructor
@Validated
@Api(value = "CRUD operation on location", tags = {"API :: Location"})
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/")
    @ApiOperation(value = "Retrieves all location information")
    public ResponseEntity<List<LocationInformation>> locations() {
        return ResponseEntity.ok(locationService.getCities());
    }

    @PostMapping("/")
    @ApiOperation(value = "Add new locations into system")
    public ResponseEntity<List<LocationInformation>> create(@Valid @RequestBody List<LocationInformation> locations) {
        return ResponseEntity.ok(locationService.saveAll(locations));
    }
}

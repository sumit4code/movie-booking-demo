package com.intuit.craft.theater.seed;

import com.intuit.craft.theater.domain.LocationInformation;
import com.intuit.craft.theater.service.LocationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Profile("seed")
@Slf4j
@AllArgsConstructor
public class SeedDataService implements CommandLineRunner {

    private final LocationService locationService;

    @Override
    public void run(String... args) throws Exception {
        List<LocationInformation> locationInformation = Arrays.asList(LocationInformation.builder().country("INDIA").city("Bangalore").build(),
                LocationInformation.builder().country("INDIA").city("Pune").build(),
                LocationInformation.builder().country("INDIA").city("Delhi").build(),
                LocationInformation.builder().country("INDIA").city("Mumbai").build(),
                LocationInformation.builder().country("INDIA").city("Kolkata").build(),
                LocationInformation.builder().country("INDIA").city("Chennai").build());
        List<LocationInformation> savedLocations = locationService.saveAll(locationInformation);
        log.info("Total records created for location {}", savedLocations);
    }
}

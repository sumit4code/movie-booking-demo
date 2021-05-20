package com.intuit.craft.theater.service;

import com.intuit.craft.exception.EntityNotFoundException;
import com.intuit.craft.theater.domain.LocationInformation;
import com.intuit.craft.theater.repository.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LocationService {

    public static final String CACHE_NAME = "location";

    private final LocationRepository locationRepository;

    @Cacheable(value = CACHE_NAME)
    public List<LocationInformation> getCities() {
        log.info("Loading all cities");
        return locationRepository.findAll();
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public List<LocationInformation> saveAll(List<LocationInformation> locationInformation) {
        List<LocationInformation> notPresentInDB = locationInformation.stream()
                .filter(locationInformation1 -> !locationRepository.findByCityAndCountry(locationInformation1.getCity(),
                        locationInformation1.getCountry()).isPresent())
                .collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(notPresentInDB) ? locationRepository.saveAll(notPresentInDB) : Collections.emptyList();
    }

    public LocationInformation getLocation(String locationId){
        return locationRepository.findById(locationId).orElseThrow(()-> new EntityNotFoundException("not a valid location"));
    }
}

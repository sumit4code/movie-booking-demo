package com.intuit.craft.theater.service;

import com.intuit.craft.exception.EntityNotFoundException;
import com.intuit.craft.exception.ValidationException;
import com.intuit.craft.theater.domain.Screen;
import com.intuit.craft.theater.domain.Theater;
import com.intuit.craft.theater.repository.TheaterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TheaterService {
    private final static String CACHE_NAME = "theater";

    private final TheaterRepository theaterRepository;

    public Theater create(Theater theater) {
        if (StringUtils.isNoneEmpty(theater.getId())) {
            throw new ValidationException("id should not be populated during create");
        }
        theater.getScreen().forEach(Screen::validate);
        Theater savedMovie = theaterRepository.insert(theater);
        log.info("theater stored successfully");
        return savedMovie;
    }

    @CachePut(value = CACHE_NAME, key = "#theater.id")
    public Theater update(String id, Theater theater) {
        if (theaterRepository.existsById(id) && id.equalsIgnoreCase(theater.getId())) {
            theater.getScreen().forEach(Screen::validate);
            return theaterRepository.save(theater);
        }
        log.error("theater doesn't exist with id {}", id);
        throw new EntityNotFoundException("Entity not found with id :" + id);
    }


    @Cacheable(value = CACHE_NAME, key = "#id")
    public Theater get(String id) {
        log.info("requesting for theater id {}", id);
        return theaterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id :" + id));
    }
}

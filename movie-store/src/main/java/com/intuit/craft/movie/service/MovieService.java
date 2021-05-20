package com.intuit.craft.movie.service;

import com.intuit.craft.exception.DuplicateEntityException;
import com.intuit.craft.exception.EntityNotFoundException;
import com.intuit.craft.exception.ValidationException;
import com.intuit.craft.movie.domain.Movie;
import com.intuit.craft.movie.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class MovieService {
    public static final String CACHE_NAME = "movieCache";

    private final MovieRepository movieRepository;

    public Movie create(Movie movie) {
        if (StringUtils.isNoneEmpty(movie.getId())) {
            throw new ValidationException("id should not be populated during create");
        }
        try{
            Movie savedMovie = movieRepository.insert(movie);
            log.info("Movie stored successfully");
            //ToDO kafka send for analytics
            return savedMovie;

        }catch (DuplicateKeyException e){
            throw new DuplicateEntityException(e.getMessage(), e);
        }
    }

    @CachePut(value = CACHE_NAME, key = "#movie.id")
    public Movie update(Movie movie) {
        if (movieRepository.existsById(movie.getId())) {
            //ToDO kafka send for analytics
            return movieRepository.save(movie);
        }
        log.error("movie doesn't exist with id {}", movie.getId());
        throw new EntityNotFoundException("Entity not found with id :" + movie.getId());

    }

    @Cacheable(value = CACHE_NAME, key = "#movieId")
    public Movie get(String movieId) {
        log.info("requesting for movie id {}", movieId);
        return movieRepository.findById(movieId).orElseThrow(() -> new EntityNotFoundException("Entity not found with id :" + movieId));
    }

    @CacheEvict(value = CACHE_NAME, key = "#movieId")
    public void delete(String movieId) {
        if (movieRepository.existsById(movieId)) {
            movieRepository.deleteById(movieId);
            log.info("movie {} successfully deleted", movieId);
        } else {
            log.error("movie doesn't exist with id {}", movieId);
            throw new EntityNotFoundException("Entity not found with id :" + movieId);
        }
    }
}

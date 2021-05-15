package com.intuit.craft;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.craft.exception.MovieBookingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class Converter {

    @Autowired
    private final ObjectMapper objectMapper;

    public String convert(Object object){
        if(object == null){
            return null;
        }
        if(object instanceof String){
            return (String) object;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new MovieBookingException("Problem occurred during conversion to string", e);
        }

    }
}
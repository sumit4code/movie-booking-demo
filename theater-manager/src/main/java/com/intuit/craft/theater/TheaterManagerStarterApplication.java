package com.intuit.craft.theater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TheaterManagerStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheaterManagerStarterApplication.class, args);
    }

}

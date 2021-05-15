package com.intuit.craft.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BookingServiceStarter {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceStarter.class, args);
    }

}

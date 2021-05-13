package com.intuit.craft.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MovieStoreStarter {

    public static void main(String[] args) {
        SpringApplication.run(MovieStoreStarter.class, args);
    }

}

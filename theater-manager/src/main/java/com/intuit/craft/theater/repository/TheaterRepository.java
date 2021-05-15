package com.intuit.craft.theater.repository;

import com.intuit.craft.theater.domain.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends MongoRepository<Theater, String> {
}

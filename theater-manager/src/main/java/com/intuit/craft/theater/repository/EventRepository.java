package com.intuit.craft.theater.repository;

import com.intuit.craft.theater.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

}

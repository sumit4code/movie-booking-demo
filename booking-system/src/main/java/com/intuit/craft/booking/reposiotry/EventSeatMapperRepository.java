package com.intuit.craft.booking.reposiotry;

import com.intuit.craft.booking.domain.EventSeatMapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventSeatMapperRepository extends MongoRepository<EventSeatMapper, String> {

    Optional<List<EventSeatMapper>> findByEventIdAndSubEventId(String eventId, String subEventId);
}

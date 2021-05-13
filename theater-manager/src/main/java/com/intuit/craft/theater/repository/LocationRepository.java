package com.intuit.craft.theater.repository;

import com.intuit.craft.theater.domain.LocationInformation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends MongoRepository<LocationInformation, String> {

    Optional<LocationInformation> findByCityAndCountry(String city, String Country);
}

package com.intuit.craft.booking.reposiotry;

import com.intuit.craft.booking.domain.LockState;
import com.intuit.craft.booking.domain.LockType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LockStateRepository extends MongoRepository<LockState, String> {

    Optional<LockState> findByLockId(String lockId);
    Optional<LockState> findByTransactionIdAndLockType(String transactionId, LockType lockType);
}

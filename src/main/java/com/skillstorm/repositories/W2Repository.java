package com.skillstorm.repositories;

import com.skillstorm.entities.W2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface W2Repository extends MongoRepository<W2, String> {
}

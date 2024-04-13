package com.skillstorm.repositories;

import com.skillstorm.entities.W2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface W2Repository extends JpaRepository<W2, Integer> {
}

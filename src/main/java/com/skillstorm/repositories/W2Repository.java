package com.skillstorm.repositories;

import com.skillstorm.entities.W2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface W2Repository extends JpaRepository<W2, Integer> {

    // Find all by User ID:
    List<W2> findAllByUserId(int userId);
}

package com.skillstorm.repositories;

import com.skillstorm.entities.TaxForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxFormRepository extends JpaRepository<TaxForm, Integer> {

    // Find TaxForm by User ID:
    Optional<TaxForm> findByUserIdAndYear(int userId, int year);
}

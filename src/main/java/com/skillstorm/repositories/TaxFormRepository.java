package com.skillstorm.repositories;

import com.skillstorm.entities.TaxForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxFormRepository extends JpaRepository<TaxForm, Integer> {

    // Find TaxForm by User ID:
    Optional<TaxForm> findByUserIdAndYear(int userId, int year);

    // Find all TaxForms by User ID:
    List<TaxForm> findAllByUserId(int userId);
}

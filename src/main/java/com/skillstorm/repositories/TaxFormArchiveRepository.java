package com.skillstorm.repositories;

import com.skillstorm.entities.TaxForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxFormArchiveRepository extends JpaRepository<TaxForm, Integer> {
}

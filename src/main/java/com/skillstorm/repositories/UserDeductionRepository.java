package com.skillstorm.repositories;

import com.skillstorm.entities.UserDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeductionRepository extends JpaRepository<UserDeduction, Integer> {

    // Find all Deductions by User ID and Year:
    List<UserDeduction> findAllByUserIdAndYear(int id, int year);
}

package com.skillstorm.repositories;

import com.skillstorm.entities.UserDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeductionRepository extends JpaRepository<UserDeduction, Integer> {
}

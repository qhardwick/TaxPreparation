package com.skillstorm.repositories;

import com.skillstorm.entities.UserCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCreditRepository extends JpaRepository<UserCredit, Integer> {

    // Find all by User ID and Year
    List<UserCredit> findAllByUserIdAndYear(int id, int year);
}

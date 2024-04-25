package com.skillstorm.repositories;

import com.skillstorm.entities.UserCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCreditRepository extends JpaRepository<UserCredit, Integer> {

    // Find all by User ID:
    List<UserCredit> findAllByUserId(int id);
}

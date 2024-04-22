package com.skillstorm.repositories;

import com.skillstorm.entities.UserCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCreditRepository extends JpaRepository<UserCredit, Integer> {
}

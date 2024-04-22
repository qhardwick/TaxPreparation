package com.skillstorm.services;

import com.skillstorm.dtos.CreditDto;

import java.util.List;

public interface CreditService {

    // Add new Credit:
    CreditDto addCredit(CreditDto credit);

    // Find Credit by Id:
    CreditDto findCreditById(int id);

    // Find all Credits:
    List<CreditDto> findAllCredits();

    // Update Credit by Id:
    CreditDto updateCreditById(int id, CreditDto updatedCredit);

    // Delete Credit by Id:
    void deleteCreditById(int id);
}

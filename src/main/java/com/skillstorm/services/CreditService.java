package com.skillstorm.services;

import com.skillstorm.dtos.CreditDto;

public interface CreditService {

    // Add new Credit:
    CreditDto addCredit(CreditDto credit);

    // Find Credit by Id:
    CreditDto findCreditById(int id);

    // Update Credit by Id:
    CreditDto updateCreditById(int id, CreditDto updatedCredit);

    // Delete Credit by Id:
    void deleteCreditById(int id);
}

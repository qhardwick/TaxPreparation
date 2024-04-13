package com.skillstorm.services;

import com.skillstorm.dtos.DeductionDto;

import java.util.List;

public interface DeductionService {

    // Add new Deduction:
    DeductionDto addDeduction(DeductionDto newDeduction);

    // Find Deduction by ID:
    DeductionDto findDeductionById(int id);

    // Find All Deductions:
    List<DeductionDto> findAllDeductions();

    // Update Deduction by ID:
    DeductionDto updateDeductionById(int id, DeductionDto updatedDeduction);

    // Delete Deduction by ID:
    void deleteDeductionById(int id);
}

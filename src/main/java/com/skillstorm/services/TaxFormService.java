package com.skillstorm.services;

import com.skillstorm.dtos.TaxFormDto;

public interface TaxFormService {

    // Add new TaxForm:
    TaxFormDto addTaxForm(TaxFormDto newTaxForm);

    // Find TaxForm by ID:
    TaxFormDto findTaxFormById(int id);

    // Update TaxForm by ID:
    TaxFormDto updateTaxFormById(int id, TaxFormDto updatedTaxForm);

    // Delete TaxForm by ID:
    void deleteTaxFormById(int id);
}

package com.skillstorm.services;

import com.skillstorm.dtos.TaxFormDto;

import java.util.List;

public interface TaxFormService {

    // Add new TaxForm:
    TaxFormDto submitTaxForm(int userId, int year);

    // Find TaxForm by ID:
    TaxFormDto findTaxFormById(int id);

    // Populate TaxForm by User ID and Year
    TaxFormDto populateTaxFormByUserId(int userId, int year);

    // Find all TaxForms by User ID:
    List<TaxFormDto> findAllTaxFormsByUserId(int userId);

    // Update TaxForm by ID:
    TaxFormDto updateTaxFormById(int id, TaxFormDto updatedTaxForm);

    // Delete TaxForm by ID:
    void deleteTaxFormById(int id);
}

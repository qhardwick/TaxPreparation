package com.skillstorm.services;

import com.skillstorm.dtos.TaxFormDto;
import com.skillstorm.entities.TaxForm;
import com.skillstorm.exceptions.TaxFormNotFoundException;
import com.skillstorm.repositories.TaxFormRepository;
import com.skillstorm.configs.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PropertySource("classpath:application.properties")
public class TaxFormServiceImpl implements TaxFormService {

    private final TaxFormRepository taxFormRepository;
    private final Environment environment;

    @Autowired
    public TaxFormServiceImpl(TaxFormRepository taxFormRepository, Environment environment) {
        this.taxFormRepository = taxFormRepository;
        this.environment = environment;
    }

    // Add new TaxForm:
    @Override
    public TaxFormDto addTaxForm(TaxFormDto newTaxForm) {
        newTaxForm.setRefund(calculateRefund(newTaxForm));
        return new TaxFormDto(taxFormRepository.saveAndFlush(newTaxForm.getTaxForm()));
    }

    // Find TaxForm by ID:
    @Override
    public TaxFormDto findTaxFormById(int id) {
        Optional<TaxForm> taxFormOptional = taxFormRepository.findById(id);
        if(!taxFormOptional.isPresent()) {
            throw new TaxFormNotFoundException(environment.getProperty(SystemMessages.TAX_FORM_NOT_FOUND.toString()));
        }
        return new TaxFormDto(taxFormOptional.get());
    }

    // Update TaxForm by ID:
    @Override
    public TaxFormDto updateTaxFormById(int id, TaxFormDto updatedTaxForm) {
        findTaxFormById(id);
        updatedTaxForm.setId(id);
        return new TaxFormDto(taxFormRepository.saveAndFlush(updatedTaxForm.getTaxForm()));
    }

    // Delete TaxForm by ID:
    @Override
    public void deleteTaxFormById(int id) {
        findTaxFormById(id);
        taxFormRepository.deleteById(id);
    }

    // Calculate federal income taxes owed:
    private double calculateFederalTaxesOwed(double totalWages) {
        if(totalWages <= 9875) {
            return totalWages * 0.1;
        }
        if(totalWages <= 40125) {
            return 987.5 + (totalWages - 9875) * 0.12;
        }
        if(totalWages <= 85525) {
            return 4617.5 + (totalWages - 40125) * 0.22;
        }
        if(totalWages <= 163300) {
            return 14605.5 + (totalWages - 85525) * 0.24;
        }
        if(totalWages <= 207350) {
            return 33271 + (totalWages - 163300) * 0.32;
        }
        if(totalWages <= 518400) {
            return 47367 + (totalWages - 207350) * 0.35;
        }

        return 156235 + (totalWages - 518400) * 0.37;
    }

    // Calculate social security taxes owed:
    private double calculateSocialSecurityTaxesOwed(double totalWages) {
        return totalWages * 0.062;
    }

    // Calculate medicare taxes owed:
    private double calculateMedicareTaxesOwed(double totalWages) {
        return totalWages * 0.0145;
    }

    // Calculate total taxes owed:
    private double calculateTotalTaxesOwed(double totalWages) {
        return calculateFederalTaxesOwed(totalWages) + calculateSocialSecurityTaxesOwed(totalWages) + calculateMedicareTaxesOwed(totalWages);
    }

    // Calculate Refund:
    // TO-DO: Add logic for Deductions and Credits
    private double calculateRefund(TaxFormDto taxForm) {

        // Combined taxes owed based on total wages. Not accounting for taxes already withheld, deductions, or credits:
        double taxesOwed = calculateTotalTaxesOwed(taxForm.getTotalWages());

        // Taxes already withheld:
        double taxesPaid = taxForm.getTotalFederalTaxesWithheld() + taxForm.getTotalSocialSecurityTaxesWithheld()
                + taxForm.getTotalMedicareTaxesWithheld();

        // Total Deductions:
        double deductions = 0; // taxForm.getTotalDeductions().stream().mapToDouble(deduction -> deduction.getAmount()).sum();

        // Taxes owed after deductions. Deductions cannot reduce taxes owed below 0:
        double taxesOwedAfterDeductions = deductions > taxesOwed ? 0 : taxesOwed - deductions;

        // Total Credits:
        double credits = 0; // taxForm.getTotalCredits().stream().mapToDouble(credit -> credit.getAmount()).sum();

        return taxesPaid + credits - taxesOwedAfterDeductions;
    }

}

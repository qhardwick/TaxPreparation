package com.skillstorm.services;

import com.skillstorm.dtos.*;
import com.skillstorm.entities.TaxForm;
import com.skillstorm.exceptions.TaxFormNotFoundException;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.TaxFormRepository;
import com.skillstorm.configs.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Service
@PropertySource("classpath:application.properties")
public class TaxFormServiceImpl implements TaxFormService {

    private final TaxFormRepository taxFormRepository;
    private final RestTemplate restTemplate;
    private final String usersUrl;
    private final Environment environment;

    @Autowired
    public TaxFormServiceImpl(TaxFormRepository taxFormRepository, RestTemplate restTemplate, @Value("${users.url}") String usersUrl, Environment environment) {
        this.taxFormRepository = taxFormRepository;
        this.restTemplate = restTemplate;
        this.usersUrl = usersUrl;
        this.environment = environment;
    }

    // Add new TaxForm:
    @Override
    public TaxFormDto addTaxForm(TaxFormDto newTaxForm) {

        return new TaxFormDto(taxFormRepository.saveAndFlush(newTaxForm.getTaxForm()));
    }

    // Find TaxForm by ID:
    @Override
    public TaxFormDto findTaxFormById(int id) {
        return taxFormRepository.findById(id).map(TaxFormDto::new)
                .orElseThrow(() -> new TaxFormNotFoundException(environment.getProperty(SystemMessages.TAX_FORM_NOT_FOUND.toString())));
    }

    // Populate the TaxForm based on the User ID:
    @Override
    public TaxFormDto populateTaxFormByUserId(int userId) {

        // Create a new TaxFormDto object:
        TaxFormDto taxFormDto = new TaxFormDto();

        // Set Wages, Taxes, Credits, and Deductions based on UserDto object:
        setFinancialData(userId, taxFormDto);

        taxFormDto.setRefund(calculateRefund(taxFormDto));
        return addTaxForm(taxFormDto);
    }

    // Utility method to update the TaxFormDto object with financial data:
    private void setFinancialData(int userId, TaxFormDto taxFormDto) {

        // Retrieve the UserDto object from the User Service:
        UserDto userDto;
        try {
            userDto = restTemplate.getForObject(usersUrl + "/" + userId, UserDto.class);
        } catch (HttpClientErrorException e) {
            throw new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString()));
        }

        BigDecimal totalWages = userDto.getW2s().stream().map(W2Dto::getWages).reduce(BigDecimal.ZERO, BigDecimal::add);
        taxFormDto.setTotalWages(totalWages);

        BigDecimal totalFederalTaxesWithheld = userDto.getW2s().stream().map(W2Dto::getFederalTaxesWithheld).reduce(BigDecimal.ZERO, BigDecimal::add);
        taxFormDto.setTotalFederalTaxesWithheld(totalFederalTaxesWithheld);

        BigDecimal totalSocialSecurityTaxesWithheld = userDto.getW2s().stream().map(W2Dto::getSocialSecurityTaxesWithheld).reduce(BigDecimal.ZERO, BigDecimal::add);
        taxFormDto.setTotalSocialSecurityTaxesWithheld(totalSocialSecurityTaxesWithheld);

        BigDecimal totalMedicareTaxesWithheld = userDto.getW2s().stream().map(W2Dto::getMedicareTaxesWithheld).reduce(BigDecimal.ZERO, BigDecimal::add);
        taxFormDto.setTotalMedicareTaxesWithheld(totalMedicareTaxesWithheld);

        setCreditsAndDeductions(userId, taxFormDto);

        // Set the UserDto object in the TaxFormDto object:
        taxFormDto.setUser(userDto);
    }

    // Utility method to update the TaxFormDto object with credits and deductions:
    private void setCreditsAndDeductions(int userId, TaxFormDto taxFormDto) {
        // Set credits based on userDto object:
        UserCreditDto[] userCreditDtoList = restTemplate.getForObject(usersUrl + "/" + userId + "/credits", UserCreditDto[].class);
        BigDecimal credits = Arrays.stream(userCreditDtoList).map(UserCreditDto::getTotalValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        taxFormDto.setCredits(credits);

        // Set deductions based on userDto object:
        UserDeductionDto[] userDeductionDtoList = restTemplate.getForObject(usersUrl + "/" + userId + "/deductions", UserDeductionDto[].class);
        BigDecimal deductions = Arrays.stream(userDeductionDtoList).map(UserDeductionDto::getDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        taxFormDto.setDeductions(deductions);
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
    private BigDecimal calculateFederalTaxesOwed(BigDecimal totalWages) {

        // If total wages are less than $9,875, the tax rate is 10%:
        if (totalWages.compareTo(new BigDecimal("9875")) != 1) {
            return totalWages.multiply(new BigDecimal("0.1"));
        }

        // If total wages are less than $40,125, the tax rate is 987.50 + 12%:
        if (totalWages.compareTo(new BigDecimal("40125")) != 1) {
            return new BigDecimal("987.5")
                    .add((totalWages.subtract(new BigDecimal("9875")))
                            .multiply(new BigDecimal("0.12")));
        }

        // If total wages are less than $85,525, the tax rate is 4,617.50 + 22%:
        if (totalWages.compareTo(new BigDecimal("85525")) != 1) {
            return new BigDecimal("4617.5")
                    .add((totalWages
                            .subtract(new BigDecimal("40125")))
                            .multiply(new BigDecimal("0.22")));
        }
        // If total wages are less than $163,300, the tax rate is 14,605.50 + 24%:
        if (totalWages.compareTo(new BigDecimal("163300")) != 1) {
            return new BigDecimal("14605.5")
                    .add((totalWages
                            .subtract(new BigDecimal("85525")))
                            .multiply(new BigDecimal("0.24")));
        }

        // If total wages are less than $207,350, the tax rate is 33,271 + 32%:
        if (totalWages.compareTo(new BigDecimal("207350")) != 1) {
            return new BigDecimal("33271")
                    .add((totalWages
                            .subtract(new BigDecimal("163300")))
                            .multiply(new BigDecimal("0.32")));
        }

        // If total wages are less than $518,400, the tax rate is 47,367 + 35%:
        if (totalWages.compareTo(new BigDecimal("518400")) != 1) {
            return new BigDecimal("47367")
                    .add((totalWages
                            .subtract(new BigDecimal("207350")))
                            .multiply(new BigDecimal("0.35")));
        }

        // If total wages are greater than $518,400, the tax rate is 156,235 + 37%:
        return new BigDecimal("156235")
                .add((totalWages
                        .subtract(new BigDecimal("518400")))
                        .multiply(new BigDecimal("0.37")));
    }

    // Calculate social security taxes owed:
    private BigDecimal calculateSocialSecurityTaxesOwed(BigDecimal totalWages) {
        return totalWages.multiply(BigDecimal.valueOf(0.062));
    }

    // Calculate medicare taxes owed:
    private BigDecimal calculateMedicareTaxesOwed(BigDecimal totalWages) {
        return totalWages.multiply(BigDecimal.valueOf(0.0145));
    }

    // Calculate total taxes owed:
    private BigDecimal calculateTotalTaxesOwed(BigDecimal totalWages) {
        return calculateFederalTaxesOwed(totalWages)
                .add(calculateSocialSecurityTaxesOwed(totalWages))
                .add(calculateMedicareTaxesOwed(totalWages));
    }

    // Calculate Refund:
    private BigDecimal calculateRefund(TaxFormDto taxForm) {

        // Combined taxes owed based on total wages. Not accounting for taxes already withheld, deductions, or credits:
        BigDecimal taxesOwed = calculateTotalTaxesOwed(taxForm.getTotalWages());

        // Taxes already withheld:
        BigDecimal taxesPaid = taxForm.getTotalFederalTaxesWithheld()
                .add(taxForm.getTotalSocialSecurityTaxesWithheld())
                .add(taxForm.getTotalMedicareTaxesWithheld());

        // Total Deductions:
        BigDecimal deductions = taxForm.getDeductions();

        // Taxes owed after deductions. Deductions cannot reduce taxes owed below 0:
        BigDecimal taxesOwedAfterDeductions = (deductions.compareTo(taxesOwed) > 0) ? BigDecimal.ZERO
                : taxesOwed.subtract(deductions);

        // Total Credits:
        BigDecimal credits = taxForm.getCredits();

        return taxesPaid.add(credits).subtract(taxesOwedAfterDeductions).setScale(2, RoundingMode.HALF_UP);
    }

}

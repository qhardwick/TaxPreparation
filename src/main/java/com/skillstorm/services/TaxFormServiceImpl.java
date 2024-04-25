package com.skillstorm.services;

import com.skillstorm.dtos.TaxFormDto;
import com.skillstorm.dtos.UserCreditDto;
import com.skillstorm.dtos.UserDeductionDto;
import com.skillstorm.dtos.UserDto;
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
    // TODO: Split into multiple smaller methods for better readability:
    @Override
    public TaxFormDto populateTaxFormByUserId(int userId) {

        // Retrieve the UserDto object from the User Service:
        UserDto userDto;
        try {
            userDto = restTemplate.getForObject(usersUrl + "/" + userId, UserDto.class);
        } catch (HttpClientErrorException e) {
            throw new UserNotFoundException(environment.getProperty(SystemMessages.USER_NOT_FOUND.toString()));
        }

        // Create a new TaxFormDto object:
        TaxFormDto taxFormDto = new TaxFormDto();
        taxFormDto.setUser(userDto);

        // Set total wages based on userDto object:
        double totalWages = userDto.getW2s().stream().map(w2Dto -> w2Dto.getWages()).reduce(0.0, Double::sum);
        taxFormDto.setTotalWages(totalWages);

        // Set total federal taxes withheld based on userDto object:
        double totalFederalTaxesWithheld = userDto.getW2s().stream().map(w2Dto -> w2Dto.getFederalTaxesWithheld()).reduce(0.0, Double::sum);
        taxFormDto.setTotalFederalTaxesWithheld(totalFederalTaxesWithheld);

        // Set total social security taxes withheld based on userDto object:
        double totalSocialSecurityTaxesWithheld = userDto.getW2s().stream().map(w2Dto -> w2Dto.getSocialSecurityTaxesWithheld()).reduce(0.0, Double::sum);
        taxFormDto.setTotalSocialSecurityTaxesWithheld(totalSocialSecurityTaxesWithheld);

        // Set total medicare taxes withheld based on userDto object:
        double totalMedicareTaxesWithheld = userDto.getW2s().stream().map(w2Dto -> w2Dto.getMedicareTaxesWithheld()).reduce(0.0, Double::sum);
        taxFormDto.setTotalMedicareTaxesWithheld(totalMedicareTaxesWithheld);

        // Set credits based on userDto object:
        UserCreditDto[] userCreditDtoList = restTemplate.getForObject(usersUrl + "/" + userId + "/credits", UserCreditDto[].class);
        double credits = Arrays.stream(userCreditDtoList).map(UserCreditDto::getTotalValue).reduce(0.0, Double::sum);
        taxFormDto.setCredits(credits);

        // Set deductions based on userDto object:
        UserDeductionDto[] userDeductionDtoList = restTemplate.getForObject(usersUrl + "/" + userId + "/deductions", UserDeductionDto[].class);
        double deductions = Arrays.stream(userDeductionDtoList).map(UserDeductionDto::getDeductionAmount).reduce(0.0, Double::sum);
        taxFormDto.setDeductions(deductions);

        taxFormDto.setRefund(calculateRefund(taxFormDto));
        return taxFormDto;
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
    private double calculateRefund(TaxFormDto taxForm) {

        // Combined taxes owed based on total wages. Not accounting for taxes already withheld, deductions, or credits:
        double taxesOwed = calculateTotalTaxesOwed(taxForm.getTotalWages());

        // Taxes already withheld:
        double taxesPaid = taxForm.getTotalFederalTaxesWithheld() + taxForm.getTotalSocialSecurityTaxesWithheld()
                + taxForm.getTotalMedicareTaxesWithheld();

        // Total Deductions:
        double deductions = taxForm.getDeductions();

        // Taxes owed after deductions. Deductions cannot reduce taxes owed below 0:
        double taxesOwedAfterDeductions = deductions > taxesOwed ? 0 : taxesOwed - deductions;

        // Total Credits:
        double credits = taxForm.getCredits();

        return taxesPaid + credits - taxesOwedAfterDeductions;
    }

}

package com.skillstorm.controllers;

import com.skillstorm.dtos.TaxFormDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.services.TaxFormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaxFormControllerTest {

    @InjectMocks private static TaxFormController taxFormController;
    @Mock private static TaxFormService taxFormService;

    private static TaxFormDto taxFormDto;
    private static TaxFormDto returnedTaxFormDto;
    private static List<TaxFormDto> taxFormDtos;

    @BeforeEach
    public void setup() {
        taxFormController = new TaxFormController(taxFormService);

        setUpTaxFormDtos();
    }

    private void setUpTaxFormDtos() {

        UserDto userDto = new UserDto();
        userDto.setId(1);

        taxFormDto = new TaxFormDto();
        taxFormDto.setUser(userDto);
        taxFormDto.setYear(2024);
        taxFormDto.setTotalWages(BigDecimal.valueOf(100000).setScale(2));
        taxFormDto.setTotalFederalTaxesWithheld(BigDecimal.valueOf(20000).setScale(2));
        taxFormDto.setTotalSocialSecurityTaxesWithheld(BigDecimal.valueOf(8000).setScale(2));
        taxFormDto.setTotalMedicareTaxesWithheld(BigDecimal.valueOf(2000).setScale(2));
        taxFormDto.setCredits(BigDecimal.valueOf(1000).setScale(2));
        taxFormDto.setDeductions(BigDecimal.valueOf(5000).setScale(2));
        taxFormDto.setRefund(BigDecimal.valueOf(5000).setScale(2));

        returnedTaxFormDto = new TaxFormDto();
        returnedTaxFormDto.setId(1);
        returnedTaxFormDto.setUser(userDto);
        returnedTaxFormDto.setYear(2024);
        returnedTaxFormDto.setTotalWages(BigDecimal.valueOf(100000).setScale(2));
        returnedTaxFormDto.setTotalFederalTaxesWithheld(BigDecimal.valueOf(20000).setScale(2));
        returnedTaxFormDto.setTotalSocialSecurityTaxesWithheld(BigDecimal.valueOf(8000).setScale(2));
        returnedTaxFormDto.setTotalMedicareTaxesWithheld(BigDecimal.valueOf(2000).setScale(2));
        returnedTaxFormDto.setCredits(BigDecimal.valueOf(1000).setScale(2));
        returnedTaxFormDto.setDeductions(BigDecimal.valueOf(5000).setScale(2));
        returnedTaxFormDto.setRefund(BigDecimal.valueOf(5000).setScale(2));

        taxFormDtos = List.of(returnedTaxFormDto);
    }

    // Test the submitTaxForm method:
    @Test
    public void submitTaxFormTest() {

        // Define stubbing:
        when(taxFormService.submitTaxForm(1, 2024)).thenReturn(returnedTaxFormDto);

        // Call the method to test:
        ResponseEntity<TaxFormDto> result = taxFormController.submitTaxForm(1, 2024);

        // Verify the results:
        assertEquals(201, result.getStatusCodeValue(), "The status code should be 201.");
        assertEquals(HttpStatus.CREATED, result.getStatusCode(), "The status should be CREATED.");
        assertEquals(1, result.getBody().getId(), "The ID should be 1.");
        assertEquals(1, result.getBody().getUser().getId(), "The User ID should be 1.");
        assertEquals(2024, result.getBody().getYear(), "The year should be 2024.");
        assertEquals(BigDecimal.valueOf(100000).setScale(2), result.getBody().getTotalWages(), "The total wages should be $100,000.00.");
        assertEquals(BigDecimal.valueOf(20000).setScale(2), result.getBody().getTotalFederalTaxesWithheld(), "The total federal taxes withheld should be $20,000.00.");
        assertEquals(BigDecimal.valueOf(8000).setScale(2), result.getBody().getTotalSocialSecurityTaxesWithheld(), "The total Social Security taxes withheld should be $8,000.00.");
        assertEquals(BigDecimal.valueOf(2000).setScale(2), result.getBody().getTotalMedicareTaxesWithheld(), "The total Medicare taxes withheld should be $2,000.00.");
        assertEquals(BigDecimal.valueOf(1000).setScale(2), result.getBody().getCredits(), "The credits should be $1,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().getDeductions(), "The deductions should be $5,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().getRefund(), "The refund should be $5,000.00.");
    }

    // Test the findTaxFormById method:
    @Test
    public void findTaxFormByIdTest() {

        // Define stubbing:
        when(taxFormService.findTaxFormById(1)).thenReturn(returnedTaxFormDto);

        // Call the method to test:
        ResponseEntity<TaxFormDto> result = taxFormController.findTaxFormById(1);

        // Verify the results:
        assertEquals(200, result.getStatusCodeValue(), "The status code should be 200.");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be OK.");
        assertEquals(1, result.getBody().getId(), "The ID should be 1.");
        assertEquals(1, result.getBody().getUser().getId(), "The User ID should be 1.");
        assertEquals(2024, result.getBody().getYear(), "The year should be 2024.");
        assertEquals(BigDecimal.valueOf(100000).setScale(2), result.getBody().getTotalWages(), "The total wages should be $100,000.00.");
        assertEquals(BigDecimal.valueOf(20000).setScale(2), result.getBody().getTotalFederalTaxesWithheld(), "The total federal taxes withheld should be $20,000.00.");
        assertEquals(BigDecimal.valueOf(8000).setScale(2), result.getBody().getTotalSocialSecurityTaxesWithheld(), "The total Social Security taxes withheld should be $8,000.00.");
        assertEquals(BigDecimal.valueOf(2000).setScale(2), result.getBody().getTotalMedicareTaxesWithheld(), "The total Medicare taxes withheld should be $2,000.00.");
        assertEquals(BigDecimal.valueOf(1000).setScale(2), result.getBody().getCredits(), "The credits should be $1,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().getDeductions(), "The deductions should be $5,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().getRefund(), "The refund should be $5,000.00.");
    }

    // Test the populateTaxFormByUserId method:
    @Test
    void populateTaxFormByUserIdTest() {

        // Define stubbing:
        when(taxFormService.populateTaxFormByUserId(1, 2024)).thenReturn(taxFormDto);

        // Call the method to test:
        ResponseEntity<TaxFormDto> result = taxFormController.populateTaxFormByUserId(1, 2024);

        // Verify the results:
        assertEquals(200, result.getStatusCodeValue(), "The status code should be 200.");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be OK.");
        assertEquals(1, result.getBody().getUser().getId(), "The User ID should be 1.");
        assertEquals(2024, result.getBody().getYear(), "The year should be 2024.");
        assertEquals(BigDecimal.valueOf(100000).setScale(2), result.getBody().getTotalWages(), "The total wages should be $100,000.00.");
        assertEquals(BigDecimal.valueOf(20000).setScale(2), result.getBody().getTotalFederalTaxesWithheld(), "The total federal taxes withheld should be $20,000.00.");
        assertEquals(BigDecimal.valueOf(8000).setScale(2), result.getBody().getTotalSocialSecurityTaxesWithheld(), "The total Social Security taxes withheld should be $8,000.00.");
        assertEquals(BigDecimal.valueOf(2000).setScale(2), result.getBody().getTotalMedicareTaxesWithheld(), "The total Medicare taxes withheld should be $2,000.00.");
        assertEquals(BigDecimal.valueOf(1000).setScale(2), result.getBody().getCredits(), "The credits should be $1,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().getDeductions(), "The deductions should be $5,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().getRefund(), "The refund should be $5,000.00.");
    }

    // Test the findAllTaxFormsByUserId method:
    @Test
    void findAllTaxFormsByUserIdTest() {

        // Define stubbing:
        when(taxFormService.findAllTaxFormsByUserId(1)).thenReturn(taxFormDtos);

        // Call the method to test:
        ResponseEntity<List<TaxFormDto>> result = taxFormController.findAllTaxFormsByUserId(1);

        // Verify the results:
        assertEquals(200, result.getStatusCodeValue(), "The status code should be 200.");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be OK.");
        assertEquals(1, result.getBody().get(0).getId(), "The ID should be 1.");
        assertEquals(1, result.getBody().get(0).getUser().getId(), "The User ID should be 1.");
        assertEquals(2024, result.getBody().get(0).getYear(), "The year should be 2024.");
        assertEquals(BigDecimal.valueOf(100000).setScale(2), result.getBody().get(0).getTotalWages(), "The total wages should be $100,000.00.");
        assertEquals(BigDecimal.valueOf(20000).setScale(2), result.getBody().get(0).getTotalFederalTaxesWithheld(), "The total federal taxes withheld should be $20,000.00.");
        assertEquals(BigDecimal.valueOf(8000).setScale(2), result.getBody().get(0).getTotalSocialSecurityTaxesWithheld(), "The total Social Security taxes withheld should be $8,000.00.");
        assertEquals(BigDecimal.valueOf(2000).setScale(2), result.getBody().get(0).getTotalMedicareTaxesWithheld(), "The total Medicare taxes withheld should be $2,000.00.");
        assertEquals(BigDecimal.valueOf(1000).setScale(2), result.getBody().get(0).getCredits(), "The credits should be $1,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().get(0).getDeductions(), "The deductions should be $5,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().get(0).getRefund(), "The refund should be $5,000.00.");
    }

    // Test the updateTaxFormById method:
    @Test
    void updateTaxFormByIdTest() {

        // Define stubbing:
        when(taxFormService.updateTaxFormById(1)).thenReturn(returnedTaxFormDto);

        // Call the method to test:
        ResponseEntity<TaxFormDto> result = taxFormController.updateTaxFormById(1);

        // Verify the results:
        assertEquals(200, result.getStatusCodeValue(), "The status code should be 200.");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be OK.");
        assertEquals(1, result.getBody().getId(), "The ID should be 1.");
        assertEquals(1, result.getBody().getUser().getId(), "The User ID should be 1.");
        assertEquals(2024, result.getBody().getYear(), "The year should be 2024.");
        assertEquals(BigDecimal.valueOf(100000).setScale(2), result.getBody().getTotalWages(), "The total wages should be $100,000.00.");
        assertEquals(BigDecimal.valueOf(20000).setScale(2), result.getBody().getTotalFederalTaxesWithheld(), "The total federal taxes withheld should be $20,000.00.");
        assertEquals(BigDecimal.valueOf(8000).setScale(2), result.getBody().getTotalSocialSecurityTaxesWithheld(), "The total Social Security taxes withheld should be $8,000.00.");
        assertEquals(BigDecimal.valueOf(2000).setScale(2), result.getBody().getTotalMedicareTaxesWithheld(), "The total Medicare taxes withheld should be $2,000.00.");
        assertEquals(BigDecimal.valueOf(1000).setScale(2), result.getBody().getCredits(), "The credits should be $1,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().getDeductions(), "The deductions should be $5,000.00.");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), result.getBody().getRefund(), "The refund should be $5,000.00.");
    }

    // Test the deleteTaxFormById method:
    @Test
    void deleteTaxFormByIdTest() {

        // Call the method to test:
        ResponseEntity<Void> result = taxFormController.deleteTaxFormById(1);

        // Verify the results:
        assertEquals(204, result.getStatusCodeValue(), "The status code should be 204.");
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode(), "The status should be NO_CONTENT.");
    }
}

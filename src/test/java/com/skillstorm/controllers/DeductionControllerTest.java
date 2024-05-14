package com.skillstorm.controllers;

import com.skillstorm.dtos.DeductionDto;
import com.skillstorm.services.DeductionService;
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
public class DeductionControllerTest {

    @InjectMocks private static DeductionController deductionController;
    @Mock private static DeductionService deductionService;

    private static DeductionDto deductionDto;
    private static DeductionDto returnedDeductionDto;
    private static List<DeductionDto> deductionDtos;

    @BeforeEach
    public void setup() {
        deductionController = new DeductionController(deductionService);

        setUpDeductions();
    }

    private void setUpDeductions() {
        deductionDto = new DeductionDto();
        deductionDto.setName("Test Deduction");
        deductionDto.setRate(BigDecimal.valueOf(0.5).setScale(2));

        returnedDeductionDto = new DeductionDto();
        returnedDeductionDto.setId(1);
        returnedDeductionDto.setName("Test Deduction");
        returnedDeductionDto.setRate(BigDecimal.valueOf(0.5).setScale(2));

        deductionDtos = List.of(returnedDeductionDto);
    }

    // Add Deduction:
    @Test
    void addDeductionTest() {

        // Define stubbing:
        when(deductionService.addDeduction(deductionDto)).thenReturn(returnedDeductionDto);

        // Call method to test:
        ResponseEntity<DeductionDto> response = deductionController.addDeduction(deductionDto);

        // Verify results:
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals(1, response.getBody().getId(), "ID should be 1");
        assertEquals("Test Deduction", response.getBody().getName(), "Name should be 'Test Deduction'");
        assertEquals(BigDecimal.valueOf(0.5).setScale(2), response.getBody().getRate(), "Rate should be 0.5");
    }

    // Find Deduction by ID:
    @Test
    void findDeductionByIdTest() {

        // Define stubbing:
        when(deductionService.findDeductionById(1)).thenReturn(returnedDeductionDto);

        // Call method to test:
        ResponseEntity<DeductionDto> response = deductionController.findDeductionById(1);

        // Verify results:
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals(1, response.getBody().getId(), "ID should be 1");
        assertEquals("Test Deduction", response.getBody().getName(), "Name should be 'Test Deduction'");
        assertEquals(BigDecimal.valueOf(0.5).setScale(2), response.getBody().getRate(), "Rate should be 0.5");
    }

    // Find All Deductions:
    @Test
    void findAllDeductionsTest() {

        // Define stubbing:
        when(deductionService.findAllDeductions()).thenReturn(deductionDtos);

        // Call method to test:
        ResponseEntity<List<DeductionDto>> response = deductionController.findAllDeductions();

        // Verify results:
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals(1, response.getBody().size(), "List size should be 1");
        assertEquals(1, response.getBody().get(0).getId(), "ID should be 1");
        assertEquals("Test Deduction", response.getBody().get(0).getName(), "Name should be 'Test Deduction'");
        assertEquals(BigDecimal.valueOf(0.5).setScale(2), response.getBody().get(0).getRate(), "Rate should be 0.5");
    }

    // Update Deduction by ID:
    @Test
    void updateDeductionByIdTest() {

        // Define stubbing:
        when(deductionService.updateDeductionById(1, deductionDto)).thenReturn(returnedDeductionDto);

        // Call method to test:
        ResponseEntity<DeductionDto> response = deductionController.updateDeductionById(1, deductionDto);

        // Verify results:
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals(1, response.getBody().getId(), "ID should be 1");
        assertEquals("Test Deduction", response.getBody().getName(), "Name should be 'Test Deduction'");
        assertEquals(BigDecimal.valueOf(0.5).setScale(2), response.getBody().getRate(), "Rate should be 0.5");
    }

    // Delete Deduction by ID:
    @Test
    void deleteDeductionByIdTest() {

        // Call method to test:
        ResponseEntity<Void> response = deductionController.deleteDeductionById(1);

        // Verify results:
        assertEquals(204, response.getStatusCodeValue(), "Status code should be 204");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status should be NO_CONTENT");
    }
}

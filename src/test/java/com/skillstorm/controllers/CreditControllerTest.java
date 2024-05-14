package com.skillstorm.controllers;

import com.skillstorm.dtos.CreditDto;
import com.skillstorm.services.CreditService;
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
class CreditControllerTest {

    @InjectMocks private static CreditController creditController;
    @Mock private static CreditService creditService;

    private static CreditDto creditDto;
    private static CreditDto returnedCreditDto;
    private static List<CreditDto> returnedCreditDtos;

    @BeforeEach
    public void setup() {
        creditController = new CreditController(creditService);

        setUpCredits();
    }

    private void setUpCredits() {
        creditDto = new CreditDto();
        creditDto.setName("Test Name");
        creditDto.setValue(BigDecimal.valueOf(1000.00).setScale(2));

        returnedCreditDto = new CreditDto();
        returnedCreditDto.setId(1);
        returnedCreditDto.setName("Test Name");
        returnedCreditDto.setValue(BigDecimal.valueOf(1000.00).setScale(2));

        returnedCreditDtos = List.of(returnedCreditDto);
    }

    // Add new Credit:
    @Test
    void testAddCredit() {

        // Define stubbing:
        when(creditService.addCredit(creditDto)).thenReturn(returnedCreditDto);

        // Call the method to test:
        ResponseEntity<CreditDto> response = creditController.addCredit(creditDto);

        // Verify the results:
        assertEquals(200, response.getStatusCode().value(), "Status code should be 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals(1, response.getBody().getId(), "Id should be 1");
        assertEquals("Test Name", response.getBody().getName(), "Name should be 'Test Name'");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), response.getBody().getValue(), "Value should be 1000.00");
    }

    // Get Credit By Id:
    @Test
    void testFindCreditById() {

        // Define stubbing:
        when(creditService.findCreditById(1)).thenReturn(returnedCreditDto);

        // Call the method to test:
        ResponseEntity<CreditDto> response = creditController.findCreditById(1);

        // Verify the results:
        assertEquals(200, response.getStatusCode().value(), "Status code should be 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals(1, response.getBody().getId(), "Id should be 1");
        assertEquals("Test Name", response.getBody().getName(), "Name should be 'Test Name'");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), response.getBody().getValue(), "Value should be 1000.00");
    }

    // Get All Credits:
    @Test
    void testFindAllCredits() {

        // Define stubbing:
        when(creditService.findAllCredits()).thenReturn(returnedCreditDtos);

        // Call the method to test:
        ResponseEntity<List<CreditDto>> response = creditController.findAllCredits();

        // Verify the results:
        assertEquals(200, response.getStatusCode().value(), "Status code should be 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals(1, response.getBody().size(), "List size should be 1");
        assertEquals(1, response.getBody().get(0).getId(), "Id should be 1");
        assertEquals("Test Name", response.getBody().get(0).getName(), "Name should be 'Test Name'");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), response.getBody().get(0).getValue(), "Value should be 1000.00");
    }

    // Update Credit By Id:
    @Test
    void testUpdateCreditById() {

        // Define stubbing:
        when(creditService.updateCreditById(1, creditDto)).thenReturn(returnedCreditDto);

        // Call the method to test:
        ResponseEntity<CreditDto> response = creditController.updateCreditById(1, creditDto);

        // Verify the results:
        assertEquals(200, response.getStatusCode().value(), "Status code should be 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals(1, response.getBody().getId(), "Id should be 1");
        assertEquals("Test Name", response.getBody().getName(), "Name should be 'Test Name'");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), response.getBody().getValue(), "Value should be 1000.00");
    }

    // Delete Credit By Id:
    @Test
    void testDeleteCreditById() {

        // Call the method to test:
        ResponseEntity<Void> response = creditController.deleteCreditById(1);

        // Verify the results:
        assertEquals(204, response.getStatusCode().value(), "Status code should be 204");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status should be NO_CONTENT");
    }
}

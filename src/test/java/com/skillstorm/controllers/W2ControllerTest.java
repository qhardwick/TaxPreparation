package com.skillstorm.controllers;

import com.skillstorm.dtos.W2Dto;
import com.skillstorm.services.W2Service;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class W2ControllerTest {

    @InjectMocks private static W2Controller w2Controller;
    @Mock private static W2Service w2Service;

    private static W2Dto newW2;
    private static W2Dto returnedW2;
    private static List<W2Dto> returnedW2s;

    @BeforeEach
    public void setup() {
        w2Controller = new W2Controller(w2Service);

        setUpW2s();
    }

    private void setUpW2s() {
        newW2 = new W2Dto();
        newW2.setEmployer("Test Employer");
        newW2.setYear(2024);
        newW2.setWages(BigDecimal.valueOf(100000));
        newW2.setFederalTaxesWithheld(BigDecimal.valueOf(10000));
        newW2.setSocialSecurityTaxesWithheld(BigDecimal.valueOf(5000));
        newW2.setMedicareTaxesWithheld(BigDecimal.valueOf(1000));
        newW2.setUserId(1);

        returnedW2 = new W2Dto();
        returnedW2.setId(1);
        returnedW2.setEmployer("Test Employer");
        returnedW2.setYear(2024);
        returnedW2.setWages(BigDecimal.valueOf(100000));
        returnedW2.setFederalTaxesWithheld(BigDecimal.valueOf(10000));
        returnedW2.setSocialSecurityTaxesWithheld(BigDecimal.valueOf(5000));
        returnedW2.setMedicareTaxesWithheld(BigDecimal.valueOf(1000));
        returnedW2.setUserId(1);

        returnedW2s = List.of(returnedW2);
    }

    // Add new W2:
    @Test
    void addW2Test() {

        // Define stubbing:
        when(w2Service.addW2(newW2)).thenReturn(returnedW2);

        // Call the method to test:
        ResponseEntity<W2Dto> response = w2Controller.addW2(newW2);

        // Verify the response:
        assertEquals(201, response.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response should be CREATED");
        assertEquals("/1", response.getHeaders().get("Location").get(0), "Should create URI: '/1");
        assertEquals(1, response.getBody().getId(), "Should return W2 with ID: 1");
        assertEquals("Test Employer", response.getBody().getEmployer(), "Should return W2 with employer: 'Test Employer'");
        assertEquals(2024, response.getBody().getYear(), "Should return W2 with year: 2024");
        assertEquals(100000, response.getBody().getWages().doubleValue(), "Should return W2 with wages: 100000");
        assertEquals(10000, response.getBody().getFederalTaxesWithheld().doubleValue(), "Should return W2 with federal taxes withheld: 10000");
        assertEquals(5000, response.getBody().getSocialSecurityTaxesWithheld().doubleValue(), "Should return W2 with social security taxes withheld: 5000");
        assertEquals(1000, response.getBody().getMedicareTaxesWithheld().doubleValue(), "Should return W2 with medicare taxes withheld: 1000");
        assertEquals(1, response.getBody().getUserId(), "Should return W2 with user ID: 1");
    }

    // Find W2 by ID:
    @Test
    void findW2ByIdTest() {

        // Define stubbing:
        when(w2Service.findW2ById(1)).thenReturn(returnedW2);

        // Call the method to test:
        ResponseEntity<W2Dto> response = w2Controller.findW2ById(1);

        // Verify the response:
        assertEquals(200, response.getStatusCode().value(), "Response should be: 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK");
        assertEquals(1, response.getBody().getId(), "Should return W2 with ID: 1");
        assertEquals("Test Employer", response.getBody().getEmployer(), "Should return W2 with employer: 'Test Employer'");
        assertEquals(2024, response.getBody().getYear(), "Should return W2 with year: 2024");
        assertEquals(100000, response.getBody().getWages().doubleValue(), "Should return W2 with wages: 100000");
        assertEquals(10000, response.getBody().getFederalTaxesWithheld().doubleValue(), "Should return W2 with federal taxes withheld: 10000");
        assertEquals(5000, response.getBody().getSocialSecurityTaxesWithheld().doubleValue(), "Should return W2 with social security taxes withheld: 5000");
        assertEquals(1000, response.getBody().getMedicareTaxesWithheld().doubleValue(), "Should return W2 with medicare taxes withheld: 1000");
        assertEquals(1, response.getBody().getUserId(), "Should return W2 with user ID: 1");
    }

    // Find all by User ID:
    @Test
    void findW2ByUserIdTest() {

        // Define stubbing:
        when(w2Service.findW2ByUserId(1)).thenReturn(returnedW2s);

        // Call the method to test:
        ResponseEntity<List<W2Dto>> response = w2Controller.findW2ByUserId(1);

        // Verify the response:
        assertEquals(200, response.getStatusCode().value(), "Response should be: 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK");
        assertEquals(1, response.getBody().size(), "Should return 1 W2");
        assertEquals(1, response.getBody().get(0).getId(), "Should return W2 with ID: 1");
        assertEquals("Test Employer", response.getBody().get(0).getEmployer(), "Should return W2 with employer: 'Test Employer'");
        assertEquals(2024, response.getBody().get(0).getYear(), "Should return W2 with year: 2024");
        assertEquals(100000, response.getBody().get(0).getWages().doubleValue(), "Should return W2 with wages: 100000");
        assertEquals(10000, response.getBody().get(0).getFederalTaxesWithheld().doubleValue(), "Should return W2 with federal taxes withheld: 10000");
        assertEquals(5000, response.getBody().get(0).getSocialSecurityTaxesWithheld().doubleValue(), "Should return W2 with social security taxes withheld: 5000");
        assertEquals(1000, response.getBody().get(0).getMedicareTaxesWithheld().doubleValue(), "Should return W2 with medicare taxes withheld: 1000");
        assertEquals(1, response.getBody().get(0).getUserId(), "Should return W2 with user ID: 1");
    }

    // Update W2 by ID:
    @Test
    void updateW2ByIdTest() {

        // Define stubbing:
        when(w2Service.updateW2ById(1, returnedW2)).thenReturn(returnedW2);

        // Call the method to test:
        ResponseEntity<W2Dto> response = w2Controller.updateW2ById(1, returnedW2);

        // Verify the response:
        assertEquals(200, response.getStatusCode().value(), "Response should be: 200");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK");
        assertEquals(1, response.getBody().getId(), "Should return W2 with ID: 1");
        assertEquals("Test Employer", response.getBody().getEmployer(), "Should return W2 with employer: 'Test Employer'");
        assertEquals(2024, response.getBody().getYear(), "Should return W2 with year: 2024");
        assertEquals(100000, response.getBody().getWages().doubleValue(), "Should return W2 with wages: 100000");
        assertEquals(10000, response.getBody().getFederalTaxesWithheld().doubleValue(), "Should return W2 with federal taxes withheld: 10000");
        assertEquals(5000, response.getBody().getSocialSecurityTaxesWithheld().doubleValue(), "Should return W2 with social security taxes withheld: 5000");
        assertEquals(1000, response.getBody().getMedicareTaxesWithheld().doubleValue(), "Should return W2 with medicare taxes withheld: 1000");
        assertEquals(1, response.getBody().getUserId(), "Should return W2 with user ID: 1");
    }

    // Delete W2 by ID:
    @Test
    void deleteW2ByIdTest() {

        // Call the method to test:
        ResponseEntity<Void> response = w2Controller.deleteW2ById(1);

        // Verify the response:
        assertEquals(204, response.getStatusCode().value(), "Response should be: 204");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Response should be NO_CONTENT");
    }

    // Upload image of W2:
    @Test
    void uploadW2ImageTest() {

        // Call the method to test:
        ResponseEntity<Void> response = w2Controller.uploadW2Image(1, new byte[0], "image/jpeg");

        // Verify the response:
        assertEquals(201, response.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response should be CREATED");
    }

    // Test downloadW2Image method
    @Test
    @SneakyThrows
    void testDownloadW2Image() {
        // Mocked image resource
        byte[] imageData = {1, 2, 3}; // Example image data
        ByteArrayResource resource = new ByteArrayResource(imageData);

        // Stubbing the downloadW2Image method
        when(w2Service.downloadW2Image(1)).thenReturn(resource);

        // Call the method to test
        W2Controller controller = new W2Controller(w2Service);
        ResponseEntity<Resource> responseEntity = controller.downloadW2Image(1);

        // Verify the response
        assertEquals(200, responseEntity.getStatusCodeValue(), "Response should be OK");
        assertEquals(imageData.length, responseEntity.getBody().contentLength(), "Content length should match");
    }
}

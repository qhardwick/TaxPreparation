package com.skillstorm.services;

import com.skillstorm.dtos.W2Dto;
import com.skillstorm.entities.W2;
import com.skillstorm.exceptions.W2NotFoundException;
import com.skillstorm.repositories.W2Repository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class W2ServiceTest {

    @InjectMocks private static W2ServiceImpl w2Service;
    @Mock private static S3Service s3Service;
    @Mock private static W2Repository w2Repository;
    @Mock private static Environment environment;

    private static W2 w2;
    private static W2Dto w2Dto;

    @BeforeEach
    public void setUp() {

        w2Service = new W2ServiceImpl(w2Repository, s3Service, environment);

        w2Dto = new W2Dto();
        w2Dto.setEmployer("Test Employer");
        w2Dto.setYear(2024);
        w2Dto.setWages(BigDecimal.valueOf(1000.00));
        w2Dto.setFederalTaxesWithheld(BigDecimal.valueOf(300.00));
        w2Dto.setSocialSecurityTaxesWithheld(BigDecimal.valueOf(200.00));
        w2Dto.setMedicareTaxesWithheld(BigDecimal.valueOf(100.00));
        w2Dto.setUserId(1);

        w2 = new W2();
        w2.setId(1);
        w2.setEmployer("Test Employer");
        w2.setYear(2024);
        w2.setWages(BigDecimal.valueOf(1000.00));
        w2.setFederalTaxesWithheld(BigDecimal.valueOf(300.00));
        w2.setSocialSecurityTaxesWithheld(BigDecimal.valueOf(200.00));
        w2.setMedicareTaxesWithheld(BigDecimal.valueOf(100.00));
        w2.setUserId(1);
    }

    @AfterEach
    public void resetMocks() {
        reset(w2Repository, environment);
    }

    // Add W2:
    @Test
    public void addW2Test() {

        //Define stubbing:
        when(w2Repository.saveAndFlush(w2Dto.getW2())).thenReturn(w2);

        //Call the method to test:
        W2Dto result = w2Service.addW2(w2Dto);

        //Verify the result:
        assertEquals(1, result.getId(), "W2 ID should be 1");
        assertEquals("Test Employer", result.getEmployer(), "Employer should be 'Test Employer'");
        assertEquals(2024, result.getYear(), "Year should be 2024");
        assertEquals(BigDecimal.valueOf(1000.00), result.getWages(), "Wages should be 1000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getFederalTaxesWithheld(), "Federal Taxes Withheld should be 300.00");
        assertEquals(BigDecimal.valueOf(200.00), result.getSocialSecurityTaxesWithheld(), "Social Security Taxes Withheld should be 200.00");
        assertEquals(BigDecimal.valueOf(100.00), result.getMedicareTaxesWithheld(), "Medicare Taxes Withheld should be 100.00");
        assertEquals(1, result.getUserId(), "User ID should be 1");
    }

    // Get W2 By Id and Succeed:
    @Test
    public void findW2ByIdSuccessTest() {

        //Define stubbing:
        when(w2Repository.findById(1)).thenReturn(Optional.of(w2));

        //Call the method to test:
        W2Dto result = w2Service.findW2ById(1);

        //Verify the result:
        assertEquals(1, result.getId(), "W2 ID should be 1");
        assertEquals("Test Employer", result.getEmployer(), "Employer should be 'Test Employer'");
        assertEquals(2024, result.getYear(), "Year should be 2024");
        assertEquals(BigDecimal.valueOf(1000.00), result.getWages(), "Wages should be 1000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getFederalTaxesWithheld(), "Federal Taxes Withheld should be 300.00");
        assertEquals(BigDecimal.valueOf(200.00), result.getSocialSecurityTaxesWithheld(), "Social Security Taxes Withheld should be 200.00");
        assertEquals(BigDecimal.valueOf(100.00), result.getMedicareTaxesWithheld(), "Medicare Taxes Withheld should be 100.00");
        assertEquals(1, result.getUserId(), "User ID should be 1");
    }

    // Get W2 By Id Not Exists:
    @Test
    public void findW2ByIdNotFoundTest() {

        //Define stubbing:
        when(w2Repository.findById(1)).thenReturn(Optional.empty());

        //Call the method to test:
        assertThrows(W2NotFoundException.class, () -> w2Service.findW2ById(1), "W2NotFoundException should be thrown");
    }

    // Find all by User ID:
    @Test
    public void findW2ByUserIdTest() {

        //Define stubbing:
        when(w2Repository.findAllByUserId(1)).thenReturn(List.of(w2));

        //Call the method to test:
        W2Dto result = w2Service.findW2ByUserId(1).get(0);

        //Verify the result:
        assertEquals(1, result.getId(), "W2 ID should be 1");
        assertEquals("Test Employer", result.getEmployer(), "Employer should be 'Test Employer'");
        assertEquals(2024, result.getYear(), "Year should be 2024");
        assertEquals(BigDecimal.valueOf(1000.00), result.getWages(), "Wages should be 1000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getFederalTaxesWithheld(), "Federal Taxes Withheld should be 300.00");
        assertEquals(BigDecimal.valueOf(200.00), result.getSocialSecurityTaxesWithheld(), "Social Security Taxes Withheld should be 200.00");
        assertEquals(BigDecimal.valueOf(100.00), result.getMedicareTaxesWithheld(), "Medicare Taxes Withheld should be 100.00");
        assertEquals(1, result.getUserId(), "User ID should be 1");
    }

    // Update W2 by ID:
    @Test
    public void updateW2ByIdTest() {

        //Define stubbing:
        when(w2Repository.findById(1)).thenReturn(Optional.of(w2));
        when(w2Repository.saveAndFlush(w2)).thenReturn(w2);

        //Call the method to test:
        W2Dto result = w2Service.updateW2ById(1, w2Dto);

        //Verify the result:
        assertEquals(1, result.getId(), "W2 ID should be 1");
        assertEquals("Test Employer", result.getEmployer(), "Employer should be 'Test Employer'");
        assertEquals(2024, result.getYear(), "Year should be 2024");
        assertEquals(BigDecimal.valueOf(1000.00), result.getWages(), "Wages should be 1000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getFederalTaxesWithheld(), "Federal Taxes Withheld should be 300.00");
        assertEquals(BigDecimal.valueOf(200.00), result.getSocialSecurityTaxesWithheld(), "Social Security Taxes Withheld should be 200.00");
        assertEquals(BigDecimal.valueOf(100.00), result.getMedicareTaxesWithheld(), "Medicare Taxes Withheld should be 100.00");
        assertEquals(1, result.getUserId(), "User ID should be 1");
    }

    // Delete W2 by ID:
    @Test
    public void deleteW2ByIdTest() {

        //Define stubbing:
        when(w2Repository.findById(1)).thenReturn(Optional.of(w2));

        //Define ArgumentCaptor:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        //Call the method to test:
        w2Service.deleteW2ById(1);

        //Verify the method was called:
        verify(w2Repository).deleteById(idCaptor.capture());

        //Verify the result:
        assertEquals(1, idCaptor.getValue(), "W2 ID should be 1");
    }

    // Upload W2 Image:
    @Test
    public void uploadW2ImageTest() {

        //Define stubbing:
        when(w2Repository.findById(1)).thenReturn(Optional.of(w2));

        // Define ArgumentCaptors:
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> imageCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<W2> w2Captor = ArgumentCaptor.forClass(W2.class);

        //Call the method to test:
        byte[] image = {0};
        w2Service.uploadW2Image(1, image, "image/png");

        //Verify the method was called:
        verify(s3Service).uploadFile(keyCaptor.capture(), imageCaptor.capture());
        verify(w2Repository).saveAndFlush(w2Captor.capture());

        // The w2Captor should capture the W2 object with the updated image key getting sent to the database:
        W2 result = w2Captor.getValue();

        //Verify the result:
        assertEquals("w2s/1/1.png", keyCaptor.getValue(), "Key should be 'w2s/1/1.png'");
        assertArrayEquals(new byte[]{0}, imageCaptor.getValue(), "Image should be a byte array");
        assertEquals(1, result.getId(), "W2 ID should be 1");
        assertEquals("Test Employer", result.getEmployer(), "Employer should be 'Test Employer'");
        assertEquals(2024, result.getYear(), "Year should be 2024");
        assertEquals(BigDecimal.valueOf(1000.00), result.getWages(), "Wages should be 1000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getFederalTaxesWithheld(), "Federal Taxes Withheld should be 300.00");
        assertEquals(BigDecimal.valueOf(200.00), result.getSocialSecurityTaxesWithheld(), "Social Security Taxes Withheld should be 200.00");
        assertEquals(BigDecimal.valueOf(100.00), result.getMedicareTaxesWithheld(), "Medicare Taxes Withheld should be 100.00");
        assertEquals(1, result.getUserId(), "User ID should be 1");
        assertEquals("w2s/1/1.png", result.getImageKey(), "Image Key should be 'w2s/1/1.png'");
    }

    // Download W2 Image Success:
    @Test
    @SneakyThrows
    public void testDownloadW2ImageSuccess() {

        byte[] imageBytes = {1, 2, 3};
        Resource resource = new ByteArrayResource(imageBytes);
        w2.setImageKey("w2s/1/1.png");

        //Define stubbing:
        when(w2Repository.findById(1)).thenReturn(Optional.of(w2));
        when(s3Service.getObject("w2s/1/1.png")).thenReturn(resource.getInputStream());

        //Call the method to test:
        Resource result = w2Service.downloadW2Image(1);

        //Verify the result:
        verify(s3Service).getObject("w2s/1/1.png");
        assertArrayEquals(new byte[]{1,2,3}, result.getContentAsByteArray(), "Should return byte array {1,2,3}");
    }
}

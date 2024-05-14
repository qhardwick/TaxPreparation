package com.skillstorm.dtos;

import com.skillstorm.entities.W2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class W2DtoTest {

    // Test the no-args constructor:
    @Test
    void testNoArgsConstructor() {
        W2Dto w2Dto = new W2Dto();
        assertEquals(0, w2Dto.getId());
        assertEquals(null, w2Dto.getEmployer());
        assertEquals(0, w2Dto.getYear());
        assertEquals(null, w2Dto.getWages());
        assertEquals(null, w2Dto.getFederalTaxesWithheld());
        assertEquals(null, w2Dto.getSocialSecurityTaxesWithheld());
        assertEquals(null, w2Dto.getMedicareTaxesWithheld());
        assertEquals(0, w2Dto.getUserId());
        assertEquals(null, w2Dto.getImageKey());
    }

    // Test the W2Dto(W2) constructor:
    @Test
    void testW2DtoConstructor() {
        W2 w2 = new W2();
        w2.setId(1);
        w2.setEmployer("Test Employer");
        w2.setYear(2024);
        w2.setWages(BigDecimal.valueOf(1000));
        w2.setFederalTaxesWithheld(BigDecimal.valueOf(100));
        w2.setSocialSecurityTaxesWithheld(BigDecimal.valueOf(100));
        w2.setMedicareTaxesWithheld(BigDecimal.valueOf(100));
        w2.setUserId(1);
        w2.setImageKey("Test Image Key");

        W2Dto w2Dto = new W2Dto(w2);

        assertEquals(1, w2Dto.getId());
        assertEquals("Test Employer", w2Dto.getEmployer());
        assertEquals(2024, w2Dto.getYear());
        assertEquals(BigDecimal.valueOf(1000), w2Dto.getWages());
        assertEquals(BigDecimal.valueOf(100), w2Dto.getFederalTaxesWithheld());
        assertEquals(BigDecimal.valueOf(100), w2Dto.getSocialSecurityTaxesWithheld());
        assertEquals(BigDecimal.valueOf(100), w2Dto.getMedicareTaxesWithheld());
        assertEquals(1, w2Dto.getUserId());
        assertEquals("Test Image Key", w2Dto.getImageKey());
    }

    // Test the getW2() method:
    @Test
    void testGetW2() {
        W2Dto w2Dto = new W2Dto();
        w2Dto.setId(1);
        w2Dto.setEmployer("Test Employer");
        w2Dto.setYear(2024);
        w2Dto.setWages(BigDecimal.valueOf(1000));
        w2Dto.setFederalTaxesWithheld(BigDecimal.valueOf(100));
        w2Dto.setSocialSecurityTaxesWithheld(BigDecimal.valueOf(100));
        w2Dto.setMedicareTaxesWithheld(BigDecimal.valueOf(100));
        w2Dto.setUserId(1);
        w2Dto.setImageKey("Test Image Key");

        W2 w2 = w2Dto.getW2();

        assertEquals(1, w2.getId());
        assertEquals("Test Employer", w2.getEmployer());
        assertEquals(2024, w2.getYear());
        assertEquals(BigDecimal.valueOf(1000), w2.getWages());
        assertEquals(BigDecimal.valueOf(100), w2.getFederalTaxesWithheld());
        assertEquals(BigDecimal.valueOf(100), w2.getSocialSecurityTaxesWithheld());
        assertEquals(BigDecimal.valueOf(100), w2.getMedicareTaxesWithheld());
        assertEquals(1, w2.getUserId());
        assertEquals("Test Image Key", w2.getImageKey());
    }
}

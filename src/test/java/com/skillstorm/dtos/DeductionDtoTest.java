package com.skillstorm.dtos;

import com.skillstorm.entities.Deduction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeductionDtoTest {

    // Test the no-args constructor:
    @Test
    void testNoArgsConstructor() {
        DeductionDto deductionDto = new DeductionDto();
        assertEquals(0, deductionDto.getId());
        assertEquals(null, deductionDto.getName());
        assertEquals(null, deductionDto.getRate());
    }

    // Test the DeductionDto(Deduction) constructor:
    @Test
    void testDeductionDtoConstructor() {
        Deduction deduction = new Deduction();
        deduction.setId(1);
        deduction.setName("Test Deduction");
        deduction.setRate(BigDecimal.valueOf(1000));

        DeductionDto deductionDto = new DeductionDto(deduction);

        assertEquals(1, deductionDto.getId());
        assertEquals("Test Deduction", deductionDto.getName());
        assertEquals(BigDecimal.valueOf(1000), deductionDto.getRate());
    }

    // Test the getDeduction method
    @Test
    void testGetDeduction() {
        DeductionDto deductionDto = new DeductionDto();
        deductionDto.setId(1);
        deductionDto.setName("Test Deduction");
        deductionDto.setRate(BigDecimal.valueOf(1000));

        Deduction deduction = deductionDto.getDeduction();

        assertEquals(1, deduction.getId());
        assertEquals("Test Deduction", deduction.getName());
        assertEquals(BigDecimal.valueOf(1000), deduction.getRate());
    }
}

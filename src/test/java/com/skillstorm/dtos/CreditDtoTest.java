package com.skillstorm.dtos;

import com.skillstorm.entities.Credit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditDtoTest {

    // Test the no-args constructor:
    @Test
    void testNoArgsConstructor() {
        CreditDto creditDto = new CreditDto();
        assertEquals(0, creditDto.getId());
        assertEquals(null, creditDto.getName());
        assertEquals(null, creditDto.getValue());
    }

    // Test the CreditDto(Credit) constructor:
    @Test
    void testCreditDtoConstructor() {
        Credit credit = new Credit();
        credit.setId(1);
        credit.setName("Test Credit");
        credit.setValue(BigDecimal.valueOf(1000));

        CreditDto creditDto = new CreditDto(credit);

        assertEquals(1, creditDto.getId());
        assertEquals("Test Credit", creditDto.getName());
        assertEquals(BigDecimal.valueOf(1000), creditDto.getValue());
    }


    // Test the getCredit method
    @Test
    void testGetCredit() {
        CreditDto creditDto = new CreditDto();
        creditDto.setId(1);
        creditDto.setName("Test Credit");
        creditDto.setValue(BigDecimal.valueOf(1000));

        Credit credit = creditDto.getCredit();

        assertEquals(1, credit.getId());
        assertEquals("Test Credit", credit.getName());
        assertEquals(BigDecimal.valueOf(1000), credit.getValue());
    }
}

package com.skillstorm.dtos;

import com.skillstorm.entities.TaxForm;
import com.skillstorm.entities.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaxFormDtoTest {

    // Test the no-args constructor:
    @Test
    public void testNoArgsConstructor() {
        TaxFormDto taxFormDto = new TaxFormDto();
        assertEquals(0, taxFormDto.getId());
        assertEquals(null, taxFormDto.getUser());
        assertEquals(0, taxFormDto.getYear());
        assertEquals(null, taxFormDto.getTotalWages());
        assertEquals(null, taxFormDto.getTotalFederalTaxesWithheld());
        assertEquals(null, taxFormDto.getTotalSocialSecurityTaxesWithheld());
        assertEquals(null, taxFormDto.getTotalMedicareTaxesWithheld());
        assertEquals(null, taxFormDto.getCredits());
        assertEquals(null, taxFormDto.getDeductions());
        assertEquals(null, taxFormDto.getRefund());
    }

    // Test the TaxFormDto(year) constructor:
    @Test
    public void testTaxFormDtoYearConstructor() {
        TaxFormDto taxFormDto = new TaxFormDto(2024);
        assertEquals(0, taxFormDto.getId());
        assertEquals(null, taxFormDto.getUser());
        assertEquals(2024, taxFormDto.getYear());
        assertEquals(null, taxFormDto.getTotalWages());
        assertEquals(null, taxFormDto.getTotalFederalTaxesWithheld());
        assertEquals(null, taxFormDto.getTotalSocialSecurityTaxesWithheld());
        assertEquals(null, taxFormDto.getTotalMedicareTaxesWithheld());
        assertEquals(null, taxFormDto.getCredits());
        assertEquals(null, taxFormDto.getDeductions());
        assertEquals(null, taxFormDto.getRefund());
    }

    // Test the TaxFormDto(TaxForm) constructor:
    @Test
    public void testTaxFormDtoConstructor() {
        TaxForm taxForm = new TaxForm();
        taxForm.setId(1);
        taxForm.setUser(new User());
        taxForm.setYear(2024);
        taxForm.setTotalWages(BigDecimal.valueOf(1000));
        taxForm.setTotalFederalTaxesWithheld(BigDecimal.valueOf(1000));
        taxForm.setTotalSocialSecurityTaxesWithheld(BigDecimal.valueOf(1000));
        taxForm.setTotalMedicareTaxesWithheld(BigDecimal.valueOf(1000));
        taxForm.setCredits(BigDecimal.valueOf(1000));
        taxForm.setDeductions(BigDecimal.valueOf(1000));
        taxForm.setRefund(BigDecimal.valueOf(1000));

        TaxFormDto taxFormDto = new TaxFormDto(taxForm);

        assertEquals(1, taxFormDto.getId());
        assertEquals(new UserDto(new User()), taxFormDto.getUser());
        assertEquals(2024, taxFormDto.getYear());
        assertEquals(BigDecimal.valueOf(1000), taxFormDto.getTotalWages());
        assertEquals(BigDecimal.valueOf(1000), taxFormDto.getTotalFederalTaxesWithheld());
        assertEquals(BigDecimal.valueOf(1000), taxFormDto.getTotalSocialSecurityTaxesWithheld());
        assertEquals(BigDecimal.valueOf(1000), taxFormDto.getTotalMedicareTaxesWithheld());
        assertEquals(BigDecimal.valueOf(1000), taxFormDto.getCredits());
        assertEquals(BigDecimal.valueOf(1000), taxFormDto.getDeductions());
        assertEquals(BigDecimal.valueOf(1000), taxFormDto.getRefund());
    }

    // Test the getTaxForm method
    @Test
    public void testGetTaxForm() {
        TaxFormDto taxFormDto = new TaxFormDto();
        taxFormDto.setId(1);
        taxFormDto.setUser(new UserDto(new User()));
        taxFormDto.setYear(2024);
        taxFormDto.setTotalWages(BigDecimal.valueOf(1000));
        taxFormDto.setTotalFederalTaxesWithheld(BigDecimal.valueOf(1000));
        taxFormDto.setTotalSocialSecurityTaxesWithheld(BigDecimal.valueOf(1000));
        taxFormDto.setTotalMedicareTaxesWithheld(BigDecimal.valueOf(1000));
        taxFormDto.setCredits(BigDecimal.valueOf(1000));
        taxFormDto.setDeductions(BigDecimal.valueOf(1000));
        taxFormDto.setRefund(BigDecimal.valueOf(1000));

        TaxForm taxForm = taxFormDto.getTaxForm();

        assertEquals(1, taxForm.getId());
        assertEquals(new User(), taxForm.getUser());
        assertEquals(2024, taxForm.getYear());
        assertEquals(BigDecimal.valueOf(1000), taxForm.getTotalWages());
        assertEquals(BigDecimal.valueOf(1000), taxForm.getTotalFederalTaxesWithheld());
        assertEquals(BigDecimal.valueOf(1000), taxForm.getTotalSocialSecurityTaxesWithheld());
        assertEquals(BigDecimal.valueOf(1000), taxForm.getTotalMedicareTaxesWithheld());
        assertEquals(BigDecimal.valueOf(1000), taxForm.getCredits());
        assertEquals(BigDecimal.valueOf(1000), taxForm.getDeductions());
        assertEquals(BigDecimal.valueOf(1000), taxForm.getRefund());
    }
}

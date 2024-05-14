package com.skillstorm.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaxFormTest {

    // Test the no-arg constructor
    @Test
    void testTaxForm() {
        TaxForm taxForm = new TaxForm();
        assertEquals(0, taxForm.getId());
        assertEquals(null, taxForm.getUser());
        assertEquals(0, taxForm.getYear());
        assertEquals(null, taxForm.getTotalWages());
        assertEquals(null, taxForm.getTotalFederalTaxesWithheld());
        assertEquals(null, taxForm.getTotalSocialSecurityTaxesWithheld());
        assertEquals(null, taxForm.getTotalMedicareTaxesWithheld());
        assertEquals(null, taxForm.getCredits());
        assertEquals(null, taxForm.getDeductions());
        assertEquals(null, taxForm.getRefund());
    }
}

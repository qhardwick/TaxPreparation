package com.skillstorm.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class W2Test {

    // Test the no-arg constructor:
    @Test
    void testW2() {
        W2 w2 = new W2();
        assertEquals(0, w2.getId());
        assertEquals(null, w2.getEmployer());
        assertEquals(0, w2.getYear());
        assertEquals(null, w2.getWages());
        assertEquals(null, w2.getFederalTaxesWithheld());
        assertEquals(null, w2.getSocialSecurityTaxesWithheld());
        assertEquals(null, w2.getMedicareTaxesWithheld());
        assertEquals(0, w2.getUserId());
        assertEquals(null, w2.getUser());
        assertEquals(null, w2.getImageKey());
    }
}

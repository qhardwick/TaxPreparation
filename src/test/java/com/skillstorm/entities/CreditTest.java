package com.skillstorm.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {

    // Test the no-args constructor:
    @Test
    void testCredit() {
        Credit credit = new Credit();
        assertEquals(0, credit.getId());
        assertEquals(null, credit.getName());
        assertEquals(null, credit.getValue());
    }
}

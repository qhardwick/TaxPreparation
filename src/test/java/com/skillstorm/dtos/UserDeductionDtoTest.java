package com.skillstorm.dtos;

import com.skillstorm.entities.Deduction;
import com.skillstorm.entities.User;
import com.skillstorm.entities.UserDeduction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDeductionDtoTest {

    private static Deduction deduction;
    private static User user;

    @BeforeEach
    public void setUp() {
        deduction = new Deduction();
        deduction.setId(1);
        deduction.setName("Test Deduction");

        user = new User();
        user.setId(1);
    }

    // Test the no-args constructor:
    @Test
    void testNoArgsConstructor() {
        UserDeductionDto userDeductionDto = new UserDeductionDto();
        assertEquals(0, userDeductionDto.getId());
        assertEquals(0, userDeductionDto.getYear());
        assertEquals(0, userDeductionDto.getUserId());
        assertEquals(0, userDeductionDto.getDeductionId());
    }

    // Test the UserDeductionDto(UserDeduction) constructor:
    @Test
    void testUserDeductionDtoConstructor() {
        UserDeduction userDeduction = new UserDeduction();
        userDeduction.setId(1);
        userDeduction.setYear(2024);
        userDeduction.setUser(user);
        userDeduction.setDeduction(deduction);
        userDeduction.setAmountSpent(BigDecimal.valueOf(100));
        userDeduction.setDeductionAmount(BigDecimal.valueOf(50));

        UserDeductionDto userDeductionDto = new UserDeductionDto(userDeduction);

        assertEquals(1, userDeductionDto.getId());
        assertEquals(2024, userDeductionDto.getYear());
        assertEquals(1, userDeductionDto.getUserId());
        assertEquals(1, userDeductionDto.getDeductionId());
        assertEquals("Test Deduction", userDeductionDto.getDeductionName());
        assertEquals(BigDecimal.valueOf(100), userDeductionDto.getAmountSpent());
        assertEquals(BigDecimal.valueOf(50), userDeductionDto.getDeductionAmount());
    }

    // Test the getUserDeduction() method:
    @Test
    void testGetUserDeduction() {
        UserDeductionDto userDeductionDto = new UserDeductionDto();
        userDeductionDto.setId(1);
        userDeductionDto.setYear(2024);
        userDeductionDto.setUserId(1);
        userDeductionDto.setDeductionId(1);
        userDeductionDto.setDeductionName("Test Deduction");
        userDeductionDto.setAmountSpent(BigDecimal.valueOf(100));
        userDeductionDto.setDeductionAmount(BigDecimal.valueOf(50));

        UserDeduction userDeduction = userDeductionDto.getUserDeduction();

        assertEquals(1, userDeduction.getId());
        assertEquals(2024, userDeduction.getYear());
        assertEquals(1, userDeduction.getUserId());
        assertEquals(1, userDeduction.getDeductionId());
        assertEquals(BigDecimal.valueOf(100), userDeduction.getAmountSpent());
        assertEquals(BigDecimal.valueOf(50), userDeduction.getDeductionAmount());
    }
}

package com.skillstorm.dtos;

import com.skillstorm.entities.Credit;
import com.skillstorm.entities.User;
import com.skillstorm.entities.UserCredit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCreditDtoTest {

    private static Credit credit;
    private static User user;

    @BeforeEach
    public void setUp() {
        credit = new Credit();
        credit.setId(1);
        credit.setName("Test Credit");
        credit.setValue(BigDecimal.valueOf(1000));

        user = new User();
        user.setId(1);
    }

    // Test the no-args constructor:
    @Test
    public void testNoArgsConstructor() {
        UserCreditDto userCreditDto = new UserCreditDto();
        assertEquals(0, userCreditDto.getId());
        assertEquals(0, userCreditDto.getYear());
        assertEquals(0, userCreditDto.getUserId());
        assertEquals(0, userCreditDto.getCreditId());
        assertEquals(1, userCreditDto.getCreditsClaimed());
    }

    // Test the UserCreditDto(UserCredit) constructor:
    @Test
    public void testUserCreditDtoConstructor() {
        UserCredit userCredit = new UserCredit();
        userCredit.setId(1);
        userCredit.setYear(2024);
        userCredit.setUser(user);
        userCredit.setCredit(credit);
        userCredit.setCreditsClaimed(1);
        userCredit.setTotalValue(credit.getValue().multiply(BigDecimal.valueOf(userCredit.getCreditsClaimed())));

        UserCreditDto userCreditDto = new UserCreditDto(userCredit);

        assertEquals(1, userCreditDto.getId());
        assertEquals(2024, userCreditDto.getYear());
        assertEquals(1, userCreditDto.getUserId());
        assertEquals(1, userCreditDto.getCreditId());
        assertEquals("Test Credit", userCreditDto.getCreditName());
        assertEquals(1, userCreditDto.getCreditsClaimed());
        assertEquals(credit.getValue(), userCreditDto.getTotalValue());
    }

    // Test the getUserCredit method
    @Test
    public void testGetUserCredit() {
        UserCreditDto userCreditDto = new UserCreditDto();
        userCreditDto.setId(1);
        userCreditDto.setYear(2024);
        userCreditDto.setUserId(1);
        userCreditDto.setCreditId(1);
        userCreditDto.setCreditName("Test Credit");
        userCreditDto.setCreditsClaimed(1);
        userCreditDto.setTotalValue(credit.getValue());

        UserCredit userCredit = userCreditDto.getUserCredit();

        assertEquals(1, userCredit.getId());
        assertEquals(2024, userCredit.getYear());
        assertEquals(1, userCredit.getCreditsClaimed());
        assertEquals(credit.getValue(), userCredit.getTotalValue());
    }
}

package com.skillstorm.services;

import com.skillstorm.dtos.*;
import com.skillstorm.entities.User;
import com.skillstorm.entities.W2;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.TaxFormArchiveRepository;
import com.skillstorm.repositories.TaxFormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaxFormServiceTest {

    @InjectMocks private static TaxFormServiceImpl taxFormService;
    @Mock private static TaxFormRepository taxFormRepository;
    @Mock private static TaxFormArchiveRepository taxFormArchiveRepository;
    @Mock private static UserService userService;
    @Spy private static Environment environment;

    private static User user;
    private static UserDto userDto;
    private static W2 w2;
    private static W2Dto w2Dto;
    private static UserCreditDto[] userCreditDtos;
    private static UserDeductionDto[] userDeductionDtos;


    @BeforeEach
    public void setup() {
        taxFormService = new TaxFormServiceImpl(taxFormRepository, taxFormArchiveRepository, userService , environment);

        setUpW2s();
        setUpUsers();
        setUpUserCreditDtos();
        setUpUserDeductionDtos();
    }

    private void setUpUserDeductionDtos() {
        UserDeductionDto userDeductionDto1 = new UserDeductionDto();
        userDeductionDto1.setId(1);
        userDeductionDto1.setUserId(1);
        userDeductionDto1.setDeductionId(1);
        userDeductionDto1.setAmountSpent(BigDecimal.valueOf(100.00));
        userDeductionDto1.setDeductionAmount(BigDecimal.valueOf(50.00));

        UserDeductionDto userDeductionDto2 = new UserDeductionDto();
        userDeductionDto2.setId(2);
        userDeductionDto2.setUserId(1);
        userDeductionDto2.setDeductionId(2);
        userDeductionDto2.setAmountSpent(BigDecimal.valueOf(200.00));
        userDeductionDto2.setDeductionAmount(BigDecimal.valueOf(100.00));

        UserDeductionDto userDeductionDto3 = new UserDeductionDto();
        userDeductionDto3.setId(3);
        userDeductionDto3.setUserId(1);
        userDeductionDto3.setDeductionId(3);
        userDeductionDto3.setAmountSpent(BigDecimal.valueOf(300.00));
        userDeductionDto3.setDeductionAmount(BigDecimal.valueOf(150.00));

        userDeductionDtos = new UserDeductionDto[] {userDeductionDto1, userDeductionDto2, userDeductionDto3};
    }

    private void setUpUserCreditDtos() {
        UserCreditDto userCreditDto1 = new UserCreditDto();
        userCreditDto1.setId(1);
        userCreditDto1.setUserId(1);
        userCreditDto1.setCreditId(1);
        userCreditDto1.setCreditsClaimed(1);
        userCreditDto1.setTotalValue(BigDecimal.valueOf(1000.00));

        UserCreditDto userCreditDto2 = new UserCreditDto();
        userCreditDto2.setId(2);
        userCreditDto2.setUserId(1);
        userCreditDto2.setCreditId(2);
        userCreditDto2.setCreditsClaimed(2);
        userCreditDto2.setTotalValue(BigDecimal.valueOf(2000.00));

        UserCreditDto userCreditDto3 = new UserCreditDto();
        userCreditDto3.setId(3);
        userCreditDto3.setUserId(1);
        userCreditDto3.setCreditId(3);
        userCreditDto3.setCreditsClaimed(3);
        userCreditDto3.setTotalValue(BigDecimal.valueOf(3000.00));

        userCreditDtos = new UserCreditDto[] {userCreditDto1, userCreditDto2, userCreditDto3};
    }

    private void setUpUsers() {
        user = new User();
        user.setId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@email.com");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setAddress("123 Test St");
        user.setPhoneNumber("123-456-7890");
        user.setSsn("123-45-6789");
        user.getW2s().add(w2);
        user.setRole("USER");

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setEmail("test@email.com");
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setAddress("123 Test St");
        userDto.setPhoneNumber("123-456-7890");
        userDto.setSsn("123-45-6789");
        userDto.getW2s().add(w2Dto);
        userDto.setRole("USER");
    }

    private void setUpW2s() {
        w2 = new W2();
        w2.setId(1);
        w2.setEmployer("Test Employer");
        w2.setYear(2024);
        w2.setWages(BigDecimal.valueOf(50000.00));
        w2.setFederalTaxesWithheld(BigDecimal.valueOf(5000, 2));
        w2.setSocialSecurityTaxesWithheld(BigDecimal.valueOf(2000, 2));
        w2.setMedicareTaxesWithheld(BigDecimal.valueOf(1000, 2));
        w2.setUserId(1);

        w2Dto = new W2Dto();
        w2Dto.setEmployer("Test Employer");
        w2Dto.setYear(2024);
        w2Dto.setWages(BigDecimal.valueOf(50000.00));
        w2Dto.setFederalTaxesWithheld(BigDecimal.valueOf(5000.00));
        w2Dto.setSocialSecurityTaxesWithheld(BigDecimal.valueOf(2000.00));
        w2Dto.setMedicareTaxesWithheld(BigDecimal.valueOf(1000.00));
        w2Dto.setUserId(1);
    }
/*
    // Populate TaxForm based on UserId but no User found:
    @Test
    public void testPopulateTaxFormThrowsUserNotFoundException() {

        // Define stubbings:
        when(taxFormRepository.findByUserId(1)).thenReturn(Optional.empty());
        doThrow(HttpClientErrorException.class).when(restTemplate).getForObject("http://localhost:8080/taxstorm/users/1", UserDto.class);

        // Verify result:
        assertThrows(UserNotFoundException.class, () -> taxFormService.populateTaxFormByUserId(1), "Should throw UserNotFoundException");
    }

    // Populate the TaxForm based on the UserID success (wages: 5,000):
    @Test
    public void testPopulateTaxForm5000Success() {

        userDto.getW2s().get(0).setWages(BigDecimal.valueOf(5000.00));

        // Define stubbings:
        when(taxFormRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1", UserDto.class)).thenReturn(userDto);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/credits", UserCreditDto[].class)).thenReturn(userCreditDtos);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/deductions", UserDeductionDto[].class)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1);

        // Verify result:
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(13417.50).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: 13417.50");
    }

    // Populate the TaxForm based on the UserID success (wages: 20,000):
    @Test
    public void testPopulateTaxForm20000Success() {

        userDto.getW2s().get(0).setWages(BigDecimal.valueOf(20000.00));

        // Define stubbings:
        when(taxFormRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1", UserDto.class)).thenReturn(userDto);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/credits", UserCreditDto[].class)).thenReturn(userCreditDtos);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/deductions", UserDeductionDto[].class)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1);

        // Verify result:
        assertEquals(BigDecimal.valueOf(20000.00), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(10567.50).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: 13417.50");
    }

    // Populate the TaxForm based on the UserID success (wages: 50,000):
    @Test
    public void testPopulateTaxForm50000Success() {

        // Define stubbings:
        when(taxFormRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1", UserDto.class)).thenReturn(userDto);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/credits", UserCreditDto[].class)).thenReturn(userCreditDtos);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/deductions", UserDeductionDto[].class)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1);

        // Verify result:
        assertEquals(BigDecimal.valueOf(50000.00), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(3685.00).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: ");
    }

    // Populate the TaxForm based on the UserID success (wages: 100,000):
    @Test
    public void testPopulateTaxForm100000Success() {

        userDto.getW2s().get(0).setWages(BigDecimal.valueOf(100000.00));

        // Define stubbings:
        when(taxFormRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1", UserDto.class)).thenReturn(userDto);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/credits", UserCreditDto[].class)).thenReturn(userCreditDtos);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/deductions", UserDeductionDto[].class)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1);

        // Verify result:
        assertEquals(BigDecimal.valueOf(100000.00), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(-11429.50).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: ");
    }

    // Populate the TaxForm based on the UserID success (wages: 200,000):
    @Test
    public void testPopulateTaxForm200000Success() {

        userDto.getW2s().get(0).setWages(BigDecimal.valueOf(200000.00));

        // Define stubbings:
        when(taxFormRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1", UserDto.class)).thenReturn(userDto);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/credits", UserCreditDto[].class)).thenReturn(userCreditDtos);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/deductions", UserDeductionDto[].class)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1);

        // Verify result:
        assertEquals(BigDecimal.valueOf(200000.00), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(-46015.00).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: ");
    }

    // Populate the TaxForm based on the UserID success (wages: 400,000):
    @Test
    public void testPopulateTaxForm400000Success() {

        userDto.getW2s().get(0).setWages(BigDecimal.valueOf(400000.00));

        // Define stubbings:
        when(taxFormRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1", UserDto.class)).thenReturn(userDto);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/credits", UserCreditDto[].class)).thenReturn(userCreditDtos);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/deductions", UserDeductionDto[].class)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1);

        // Verify result:
        assertEquals(BigDecimal.valueOf(400000.00), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(-131094.50).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: ");
    }

    // Populate the TaxForm based on the UserID success (wages: 1,000,000):
    @Test
    public void testPopulateTaxForm1000000Success() {

        userDto.getW2s().get(0).setWages(BigDecimal.valueOf(1000000.00));

        // Define stubbings:
        when(taxFormRepository.findByUserId(1)).thenReturn(Optional.empty());
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1", UserDto.class)).thenReturn(userDto);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/credits", UserCreditDto[].class)).thenReturn(userCreditDtos);
        when(restTemplate.getForObject("http://localhost:8080/taxstorm/users/1/deductions", UserDeductionDto[].class)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1);

        // Verify result:
        assertEquals(BigDecimal.valueOf(1000000.00), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(-396627.00).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: ");
    }
    
 */
}

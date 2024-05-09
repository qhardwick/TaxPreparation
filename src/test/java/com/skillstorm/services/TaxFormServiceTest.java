package com.skillstorm.services;

import com.skillstorm.dtos.*;
import com.skillstorm.entities.TaxForm;
import com.skillstorm.entities.User;
import com.skillstorm.entities.W2;
import com.skillstorm.exceptions.TaxFormNotFoundException;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.TaxFormArchiveRepository;
import com.skillstorm.repositories.TaxFormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaxFormServiceTest {

    @InjectMocks private static TaxFormServiceImpl taxFormService;
    @Mock private static TaxFormRepository taxFormRepository;
    @Mock private static TaxFormArchiveRepository taxFormArchiveRepository;
    @Mock private static UserService userService;
    @Spy private static Environment environment;

    private static TaxForm returnedTaxForm;
    private static List<TaxForm> taxForms;

    private static User user;
    private static UserDto userDto;

    private static W2 w2;
    private static W2Dto w2Dto;

    private static List<UserCreditDto> userCreditDtos;
    private static List<UserDeductionDto> userDeductionDtos;


    @BeforeEach
    public void setup() {
        taxFormService = new TaxFormServiceImpl(taxFormRepository, taxFormArchiveRepository, userService , environment);

        setUpW2s();
        setUpUsers();
        setUpUserCreditDtos();
        setUpUserDeductionDtos();
        setUpTaxForms();
    }

    private void setUpTaxForms() {
        returnedTaxForm = new TaxForm();
        returnedTaxForm.setId(1);
        returnedTaxForm.setUser(user);
        returnedTaxForm.setYear(2024);
        returnedTaxForm.setTotalWages(BigDecimal.valueOf(50000.00).setScale(2, RoundingMode.HALF_UP));
        returnedTaxForm.setTotalFederalTaxesWithheld(BigDecimal.valueOf(5000.00).setScale(2, RoundingMode.HALF_UP));
        returnedTaxForm.setTotalSocialSecurityTaxesWithheld(BigDecimal.valueOf(2000.00).setScale(2, RoundingMode.HALF_UP));
        returnedTaxForm.setTotalMedicareTaxesWithheld(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP));
        returnedTaxForm.setCredits(BigDecimal.valueOf(6000.00).setScale(2, RoundingMode.HALF_UP));
        returnedTaxForm.setDeductions(BigDecimal.valueOf(300.00).setScale(2, RoundingMode.HALF_UP));
        returnedTaxForm.setRefund(BigDecimal.valueOf(3685.00).setScale(2, RoundingMode.HALF_UP));

        taxForms = List.of(returnedTaxForm);
    }

    private void setUpUserDeductionDtos() {
        UserDeductionDto userDeductionDto1 = new UserDeductionDto();
        userDeductionDto1.setId(1);
        userDeductionDto1.setYear(2024);
        userDeductionDto1.setUserId(1);
        userDeductionDto1.setDeductionId(1);
        userDeductionDto1.setAmountSpent(BigDecimal.valueOf(100.00));
        userDeductionDto1.setDeductionAmount(BigDecimal.valueOf(50.00));

        UserDeductionDto userDeductionDto2 = new UserDeductionDto();
        userDeductionDto2.setId(2);
        userDeductionDto2.setYear(2024);
        userDeductionDto2.setUserId(1);
        userDeductionDto2.setDeductionId(2);
        userDeductionDto2.setAmountSpent(BigDecimal.valueOf(200.00));
        userDeductionDto2.setDeductionAmount(BigDecimal.valueOf(100.00));

        UserDeductionDto userDeductionDto3 = new UserDeductionDto();
        userDeductionDto3.setId(3);
        userDeductionDto3.setYear(2024);
        userDeductionDto3.setUserId(1);
        userDeductionDto3.setDeductionId(3);
        userDeductionDto3.setAmountSpent(BigDecimal.valueOf(300.00));
        userDeductionDto3.setDeductionAmount(BigDecimal.valueOf(150.00));

        userDeductionDtos = new ArrayList<>();
        userDeductionDtos.add(userDeductionDto1);
        userDeductionDtos.add(userDeductionDto2);
        userDeductionDtos.add(userDeductionDto3);
    }

    private void setUpUserCreditDtos() {
        UserCreditDto userCreditDto1 = new UserCreditDto();
        userCreditDto1.setId(1);
        userCreditDto1.setYear(2024);
        userCreditDto1.setUserId(1);
        userCreditDto1.setCreditId(1);
        userCreditDto1.setCreditsClaimed(1);
        userCreditDto1.setTotalValue(BigDecimal.valueOf(1000.00));

        UserCreditDto userCreditDto2 = new UserCreditDto();
        userCreditDto2.setId(2);
        userCreditDto2.setYear(2024);
        userCreditDto2.setUserId(1);
        userCreditDto2.setCreditId(2);
        userCreditDto2.setCreditsClaimed(2);
        userCreditDto2.setTotalValue(BigDecimal.valueOf(2000.00));

        UserCreditDto userCreditDto3 = new UserCreditDto();
        userCreditDto3.setId(3);
        userCreditDto3.setYear(2024);
        userCreditDto3.setUserId(1);
        userCreditDto3.setCreditId(3);
        userCreditDto3.setCreditsClaimed(3);
        userCreditDto3.setTotalValue(BigDecimal.valueOf(3000.00));

        userCreditDtos = new ArrayList<>();
        userCreditDtos.add(userCreditDto1);
        userCreditDtos.add(userCreditDto2);
        userCreditDtos.add(userCreditDto3);
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

    // Submit the TaxForm based on the UserID success:
    @Test
    public void submitTaxFormTest() {

        // Define stubbings:
        when(taxFormRepository.findByUserIdAndYear(1, 2024)).thenReturn(Optional.empty());
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);
        when(taxFormRepository.saveAndFlush(any(TaxForm.class))).thenReturn(returnedTaxForm);

        // Call the method to test:
        TaxFormDto result = taxFormService.submitTaxForm(1, 2024);

        // Verify result:
        assertEquals(1, result.getId(), "ID should be: 1");
        assertEquals(1, result.getUser().getId(), "User ID should be: 1");
        assertEquals(2024, result.getYear(), "Year should be: 2024");
        assertEquals(BigDecimal.valueOf(50000.00).setScale(2), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00).setScale(2), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00).setScale(2), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00).setScale(2), result.getCredits(), "Total tax Credits");
    }

    // Find the TaxForm based on ID success:
    @Test
    public void findTaxFormByUserIdSuccessTest() {

        // Define stubbings:
        when(taxFormRepository.findById(1)).thenReturn(Optional.of(returnedTaxForm));

        // Call the method to test:
        TaxFormDto result = taxFormService.findTaxFormById(1);

        // Verify result:
        assertEquals(1, result.getId(), "ID should be: 1");
        assertEquals(1, result.getUser().getId(), "User ID should be: 1");
        assertEquals(2024, result.getYear(), "Year should be: 2024");
        assertEquals(BigDecimal.valueOf(50000.00).setScale(2), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00).setScale(2), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00).setScale(2), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00).setScale(2), result.getCredits(), "Total tax Credits");
    }

    // Find the TaxForm based on ID failure:
    @Test
    public void findTaxFormByUserIdFailureTest() {

        // Define stubbings:
        when(taxFormRepository.findById(1)).thenReturn(Optional.empty());

        // Call the method to test:
        assertThrows(TaxFormNotFoundException.class, () -> taxFormService.findTaxFormById(1));
    }

    // Populate the TaxForm based on the UserID success (wages: 5,000):
    @Test
    public void testPopulateTaxForm5000Success() {

        userDto.getW2s().get(0).setWages(BigDecimal.valueOf(5000.00));

        // Define stubbings:
        when(taxFormRepository.findByUserIdAndYear(1, 2024)).thenReturn(Optional.empty());
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1, 2024);

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
        when(taxFormRepository.findByUserIdAndYear(1,2024)).thenReturn(Optional.empty());
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1, 2024);

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
        when(taxFormRepository.findByUserIdAndYear(1,2024)).thenReturn(Optional.empty());
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1, 2024);

        // Verify result:
        assertEquals(BigDecimal.valueOf(50000.0), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.0), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.0), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.0), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.0), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.0), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(3685.00).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: ");
    }

    // Populate the TaxForm based on the UserID success (wages: 100,000):
    @Test
    public void testPopulateTaxForm100000Success() {

        userDto.getW2s().get(0).setWages(BigDecimal.valueOf(100000.00));

        // Define stubbings:
        when(taxFormRepository.findByUserIdAndYear(1,2024)).thenReturn(Optional.empty());
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1, 2024);

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
        when(taxFormRepository.findByUserIdAndYear(1, 2024)).thenReturn(Optional.empty());
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1, 2024);

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
        when(taxFormRepository.findByUserIdAndYear(1, 2024)).thenReturn(Optional.empty());
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1, 2024);

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
        when(taxFormRepository.findByUserIdAndYear(1, 2024)).thenReturn(Optional.empty());
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);

        // Call the method to test:
        TaxFormDto result = taxFormService.populateTaxFormByUserId(1, 2024);

        // Verify result:
        assertEquals(BigDecimal.valueOf(1000000.00), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00), result.getCredits(), "Total tax Credits should be: 6000.00");
        assertEquals(BigDecimal.valueOf(300.00), result.getDeductions(), "Total tax Deductions should be: 300.00");
        assertEquals(BigDecimal.valueOf(-396627.00).setScale(2, RoundingMode.HALF_UP), result.getRefund(), "Total tax Refund should be: ");
    }

    // Find all TaxForms based on the UserID:
    @Test
    public void testFindAllTaxFormsByUserId() {

        // Define stubbings:
        when(taxFormRepository.findAllByUserId(1)).thenReturn(taxForms);

        // Call the method to test:
        List<TaxFormDto> result = taxFormService.findAllTaxFormsByUserId(1);

        // Verify result:
        assertEquals(1, result.size(), "Size should be: 1");
        assertEquals(1, result.get(0).getId(), "ID should be: 1");
        assertEquals(1, result.get(0).getUser().getId(), "User ID should be: 1");
        assertEquals(2024, result.get(0).getYear(), "Year should be: 2024");
        assertEquals(BigDecimal.valueOf(50000.00).setScale(2), result.get(0).getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00).setScale(2), result.get(0).getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00).setScale(2), result.get(0).getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), result.get(0).getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00).setScale(2), result.get(0).getCredits(), "Total tax Credits");
    }

    // Update the TaxForm by ID:
    @Test
    public void testUpdateTaxFormById() {

        // Define stubbings:
        when(taxFormRepository.findById(1)).thenReturn(Optional.of(returnedTaxForm));
        when(taxFormRepository.findByUserIdAndYear(1,2024)).thenReturn(Optional.of(returnedTaxForm));
        when(userService.findUserById(1)).thenReturn(userDto);
        when(userService.findAllCreditsByUserId(1)).thenReturn(userCreditDtos);
        when(userService.findAllDeductionsByUserId(1)).thenReturn(userDeductionDtos);
        when(taxFormRepository.saveAndFlush(any(TaxForm.class))).thenReturn(returnedTaxForm);

        // Call the method to test:
        TaxFormDto result = taxFormService.updateTaxFormById(1);

        // Verify result:
        assertEquals(1, result.getId(), "ID should be: 1");
        assertEquals(1, result.getUser().getId(), "User ID should be: 1");
        assertEquals(2024, result.getYear(), "Year should be: 2024");
        assertEquals(BigDecimal.valueOf(50000.00).setScale(2), result.getTotalWages(), "Total wages should be: 50,000.00");
        assertEquals(BigDecimal.valueOf(5000.00).setScale(2), result.getTotalFederalTaxesWithheld(), "Total federal taxes withheld should be: 5000.00");
        assertEquals(BigDecimal.valueOf(2000.00).setScale(2), result.getTotalSocialSecurityTaxesWithheld(), "Social Security taxes withheld should be: 2000.00");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), result.getTotalMedicareTaxesWithheld(), "Total Medicare taxes withheld should be: 1000.00");
        assertEquals(BigDecimal.valueOf(6000.00).setScale(2), result.getCredits(), "Total tax Credits");
    }

    // Delete the TaxForm by ID:
    @Test
    public void testDeleteTaxFormById() {

        // Define stubbings:
        when(taxFormRepository.findById(1)).thenReturn(Optional.of(returnedTaxForm));

        // Call the method to test:
        taxFormService.deleteTaxFormById(1);

        // Verify result:
        verify(taxFormRepository, times(1)).deleteById(1);
    }
}

package com.skillstorm.services;

import com.skillstorm.dtos.*;
import com.skillstorm.entities.User;
import com.skillstorm.entities.UserCredit;
import com.skillstorm.entities.UserDeduction;
import com.skillstorm.exceptions.CreditNotFoundException;
import com.skillstorm.exceptions.DeductionNotFoundException;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.UserCreditRepository;
import com.skillstorm.repositories.UserDeductionRepository;
import com.skillstorm.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks private static UserServiceImpl userService;
    @Mock private static UserRepository userRepository;
    @Mock private static UserCreditRepository userCreditRepository;
    @Mock private static UserDeductionRepository userDeductionRepository;
    @Mock private static CreditService creditService;
    @Mock private static DeductionService deductionService;
    @Spy private PasswordEncoder passwordEncoder;
    @Spy private static Environment environment;

    private static UserDto userDto;
    private static User user;

    private static CreditDto creditDto;
    private static UserCredit userCredit;
    private static UserCreditDto userCreditDto;

    private static DeductionDto deductionDto;
    private static UserDeduction userDeduction;
    private static UserDeductionDto userDeductionDto;


    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, userCreditRepository, userDeductionRepository, creditService, deductionService, passwordEncoder, environment);

        setUpUsers();
        setUpCredits();
        setUpDeductions();
    }

    private void setUpDeductions() {
        deductionDto = new DeductionDto();
        deductionDto.setId(1);
        deductionDto.setName("testCredit");
        deductionDto.setRate(BigDecimal.valueOf(0.50).setScale(2));

        userDeduction = new UserDeduction();
        userDeduction.setId(1);
        userDeduction.setUserId(1);
        userDeduction.setUser(user);
        userDeduction.setDeductionId(1);
        userDeduction.setDeduction(deductionDto.getDeduction());
        userDeduction.setAmountSpent(BigDecimal.valueOf(1000.00).setScale(2));
        userDeduction.setDeductionAmount(BigDecimal.valueOf(500.00).setScale(2));

        userDeductionDto = new UserDeductionDto();
        userDeductionDto.setUserId(1);
        userDeductionDto.setDeductionId(1);
        userDeductionDto.setAmountSpent(BigDecimal.valueOf(1000.00).setScale(2));
    }

    private void setUpCredits() {
        creditDto = new CreditDto();
        creditDto.setId(1);
        creditDto.setName("testCredit");
        creditDto.setValue(BigDecimal.valueOf(1000.00).setScale(2));

        userCredit = new UserCredit();
        userCredit.setId(1);
        userCredit.setUserId(1);
        userCredit.setUser(user);
        userCredit.setCreditId(1);
        userCredit.setCredit(creditDto.getCredit());
        userCredit.setCreditsClaimed(2);
        userCredit.setTotalValue(BigDecimal.valueOf(2000.00).setScale(2));

        userCreditDto = new UserCreditDto();
        userCreditDto.setUserId(1);
        userCreditDto.setCreditId(1);
        userCreditDto.setCreditsClaimed(2);
    }

    private void setUpUsers() {
        userDto = new UserDto();
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setEmail("email@test.com");
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setAddress("123 Test St");
        userDto.setPhoneNumber("123-456-7890");
        userDto.setSsn("123-45-6789");

        user = new User();
        user.setId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("email@test.com");
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAddress("123 Test St");
        user.setPhoneNumber("123-456-7890");
        user.setSsn("123-45-6789");
        user.setRole("USER");
    }

    // Add new User:
    @Test
    public void addUserSuccessTest() {

        // Define Stubbing:
        User userToSave = userDto.getUser();
        userToSave.setPassword(passwordEncoder.encode("password"));
        when(userRepository.saveAndFlush(userToSave)).thenReturn(user);

        // Call the method to test:
        UserDto result = userService.addUser(userDto);

        // Verify the results:
        assertEquals(1, result.getId(), "User ID should be 1");
        assertEquals("Test", result.getFirstName(), "First name should be Test");
        assertEquals("User", result.getLastName(), "Last name should be User");
        assertEquals("email@test.com", result.getEmail(), "Email should be email@test.com");
        assertEquals("testuser", result.getUsername(), "Username should be testuser");
        assertEquals("123 Test St", result.getAddress(), "Address should be 123 Test St");
        assertEquals("123-456-7890", result.getPhoneNumber(), "Phone number should be 123-456-7890");
        assertEquals("123-45-6789", result.getSsn(), "SSN should be 123-45-6789");
        assertEquals(0, result.getW2s().size(), "W2s should be empty");
    }

    // Add User violates unique constraint:
    @Test
    public void addUserThrowsIllegalArgumentException() {

        // Define Stubbing:
        User userToSave = userDto.getUser();
        userToSave.setPassword(passwordEncoder.encode("password"));
        doThrow(DataIntegrityViolationException.class).when(userRepository).saveAndFlush(userToSave);

        // Verify the result:
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(userDto), "Should throw IllegalArgumentException.class");
    }


    // Find User by ID Success:
    @Test
    public void findUserByIdSuccessTest() {

        // Define Stubbing:
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

        // Call the method to test:
        UserDto result = userService.findUserById(1);

        // Verify the results:
        assertEquals(1, result.getId(), "User ID should be 1");
        assertEquals("Test", result.getFirstName(), "First name should be Test");
        assertEquals("User", result.getLastName(), "Last name should be User");
        assertEquals("email@test.com", result.getEmail(), "Email should be email@test.com");
        assertEquals("testuser", result.getUsername(), "Username should be testuser");
        assertEquals("123 Test St", result.getAddress(), "Address should be 123 Test St");
        assertEquals("123-456-7890", result.getPhoneNumber(), "Phone number should be 123-456-7890");
        assertEquals("123-45-6789", result.getSsn(), "SSN should be 123-45-6789");
    }

    // Find User by ID Not Found:
    @Test
    public void findUserByIdNotFoundTest() {

        // Define Stubbing:
        when(userRepository.findById(1)).thenReturn(java.util.Optional.empty());

        // Call the method to test:
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(1), "UserNotFoundException should be thrown");
    }

    // Load User by Username Success:
    @Test
    public void loadUserByUsernameSuccessTest() {

        // Define stubbing:
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Call method to test:
        UserDetails result = userService.loadUserByUsername(user.getUsername());

        // Verify result:
        assertEquals("testuser", result.getUsername(), "Username should be: testuser");
    }

    // Load User by Username User Not Found:
    @Test
    public void loadUserByUsernameThrowsUsernameNotFoundTest() {

        // Define stubbing:
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Verify result:
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getUsername()), "Should throw UsernameNotFoundException");
    }

    // Update User by ID:
    @Test
    public void updateUserByIdTest() {

        // Define Stubbing:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(user)).thenReturn(user);

        // Call the method to test:
        UserDto result = userService.updateUserById(1, userDto);

        // Verify the results:
        assertEquals(1, result.getId(), "User ID should be 1");
        assertEquals("Test", result.getFirstName(), "First name should be Test");
        assertEquals("User", result.getLastName(), "Last name should be User");
        assertEquals("email@test.com", result.getEmail(), "Email should be email@test.com");
        assertEquals("testuser", result.getUsername(), "Username should be testuser");
        assertEquals("123 Test St", result.getAddress(), "Address should be 123 Test St");
        assertEquals("123-456-7890", result.getPhoneNumber(), "Phone number should be 123-456-7890");
        assertEquals("123-45-6789", result.getSsn(), "SSN should be 123-45-6789");
    }

    // Update Password Success:
    @Test
    public void updatePasswordSuccess() {

        // Define stubbing:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Define ArgumentCaptor:
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // Call method to test:
        userService.updatePasswordById(1, "hello");

        // Capture the result:
        verify(userRepository).saveAndFlush(userArgumentCaptor.capture());

        // Verify what was sent to the database:
        String newPassword = passwordEncoder.encode("hello");
        assertEquals(newPassword, userArgumentCaptor.getValue().getPassword(), "Password should be encoded");

    }

    // Update Password User Not Found:
    @Test
    public void updatePasswordThrowsUserNotFoundTest() {

        // Define stubbing:
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Verify the result:
        assertThrows(UserNotFoundException.class, () -> userService.updatePasswordById(1, "hello"), "Should throw UserNotFoundException");
    }

    // Delete User by ID:
    @Test
    public void deleteUserByIdTest() {

        // Define Stubbing:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Define Argument Captor:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        // Call the method to test:
        userService.deleteUserById(1);

        // Verify the the method was called:
        verify(userRepository).deleteById(idCaptor.capture());

        // Verify the results:
        assertEquals(1, idCaptor.getValue(), "ID should be 1");
    }

    // Add Tax Credit to User Success:
    @Test
    public void addTaxCreditSuccessTest() {

        UserCredit userCreditToSave = userCreditDto.getUserCredit();
        userCreditToSave.setTotalValue(BigDecimal.valueOf(2000.00).setScale(2));
        userCreditToSave.setUser(user);
        userCreditToSave.setCredit(creditDto.getCredit());

        // Define Stubbings:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(creditService.findCreditById(1)).thenReturn(creditDto);
        when(userCreditRepository.saveAndFlush(userCreditToSave)).thenReturn(userCredit);

        // Call the method to test:
        UserCreditDto result = userService.addTaxCredit(1, userCreditDto);

        // Verify result:
        assertEquals(1, result.getId(), "UserCredit.id should be: 1");
        assertEquals(1, result.getUserId(), "UserCredit.userId should be: 1");
        assertEquals(1, result.getCreditId(), "UserCredit.creditId should be: 1");
        assertEquals(2, result.getCreditsClaimed(), "Number of credits claimed should be: 2");
        assertEquals(BigDecimal.valueOf(2000.00).setScale(2, RoundingMode.HALF_UP), result.getTotalValue(), "Total value should be: 2000.00");
    }

    // Find all Credits claimed by a User:
    @Test
    public void findAllCreditsByUserIdTest() {

        // Define stubbing:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userCreditRepository.findAllByUserId(1)).thenReturn(List.of(userCredit));

        // Call method to test:
        List<UserCreditDto> result = userService.findAllCreditsByUserId(1);

        // Verify the result:
        assertEquals(1, result.size(), "Should return a list of size: 1");
    }

    // Add Tax Deduction to User Success:
    @Test
    public void addTaxDeductionSuccessTest() {

        UserDeduction userDeductionToSave = userDeductionDto.getUserDeduction();
        userDeductionToSave.setDeductionAmount(BigDecimal.valueOf(500.00).setScale(2));
        userDeductionToSave.setUser(user);
        userDeductionToSave.setDeduction(deductionDto.getDeduction());

        // Define Stubbings:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(deductionService.findDeductionById(1)).thenReturn(deductionDto);
        when(userDeductionRepository.saveAndFlush(userDeductionToSave)).thenReturn(userDeduction);

        // Call the method to test:
        UserDeductionDto result = userService.addDeduction(1, userDeductionDto);

        // Verify result:
        assertEquals(1, result.getId(), "UserDeduction.id should be: 1");
        assertEquals(1, result.getUserId(), "UserDeduction.userId should be: 1");
        assertEquals(1, result.getDeductionId(), "UserDeduction.creditId should be: 1");
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP), result.getAmountSpent(), "Amount spent should be: 1000.00");
        assertEquals(BigDecimal.valueOf(500.00).setScale(2, RoundingMode.HALF_UP), result.getDeductionAmount(), "Total deduction should be: 500.00");
    }

    // Find all Deductions claimed by a User:
    @Test
    public void findAllDeductionsByUserIdTest() {

        // Define stubbing:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userDeductionRepository.findAllByUserId(1)).thenReturn(List.of(userDeduction));

        // Call method to test:
        List<UserDeductionDto> result = userService.findAllDeductionsByUserId(1);

        // Verify the result:
        assertEquals(1, result.size(), "Should return a list of size: 1");
    }
}
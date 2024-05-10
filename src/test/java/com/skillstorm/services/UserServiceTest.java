package com.skillstorm.services;

import com.skillstorm.dtos.*;
import com.skillstorm.entities.User;
import com.skillstorm.entities.UserCredit;
import com.skillstorm.entities.UserDeduction;
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
        userDto.setUsername("email@test.com");
        userDto.setPassword("password");
        userDto.setAddress("123 Test St");
        userDto.setPhoneNumber("123-456-7890");
        userDto.setSsn("123-45-6789");

        user = new User();
        user.setId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("email@test.com");
        user.setUsername("email@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAddress("123 Test St");
        user.setPhoneNumber("123-456-7890");
        user.setSsn("123-45-6789");
        user.setRole("USER");
    }

    // Add new User:
    @Test
    public void addUserSuccessTest() {

        // User dto for method call:
        userDto = new UserDto();
        userDto.setEmail("email@test.com");
        userDto.setPassword("password");

        // User that will be saved:
        User userToSave = userDto.getUser();
        userToSave.setUsername("email@test.com");
        userToSave.setPassword(passwordEncoder.encode("password"));

        // User that will be returned:
        user = new User();
        user.setId(1);
        user.setEmail("email@test.com");
        user.setUsername("email@test.com");

        // Define Stubbing:
        when(userRepository.saveAndFlush(userToSave)).thenReturn(user);

        // Call the method to test:
        UserDto result = userService.addUser(userDto);

        // Verify the results:
        assertEquals(1, result.getId(), "User ID should be 1");
        assertEquals("email@test.com", result.getEmail(), "Email should be email@test.com");
        assertEquals("email@test.com", result.getUsername(), "Username should be email@test.com");
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

    // Add new Admin Success:
    @Test
    public void addAdminSuccessTest() {

        // User dto for method call:
        userDto = new UserDto();
        userDto.setEmail("email@test.com");
        userDto.setPassword("password");

        // User that will be saved:
        User userToSave = userDto.getUser();
        userToSave.setUsername("email@test.com");
        userToSave.setPassword(passwordEncoder.encode("password"));
        userToSave.setRole("ADMIN");

        // User that will be returned:
        user = new User();
        user.setId(1);
        user.setEmail("email@test.com");
        user.setUsername("email@test.com");
        user.setRole("ADMIN");

        // Define Stubbing:
        when(userRepository.saveAndFlush(userToSave)).thenReturn(user);

        // Call the method to test:
        UserDto result = userService.addAdmin(userDto);

        // Verify the results:
        assertEquals(1, result.getId(), "User ID should be 1");
        assertEquals("email@test.com", result.getEmail(), "Email should be email@test.com");
        assertEquals("email@test.com", result.getUsername(), "Username should be email@test.com");
        assertEquals("ADMIN", result.getRole(), "Role should be ADMIN");
        assertEquals(0, result.getW2s().size(), "W2s should be empty");
    }

    // Add Admin violates unique constraint:
    @Test
    public void addAdminThrowsIllegalArgumentException() {

        // Define Stubbing:
        User userToSave = userDto.getUser();
        userToSave.setPassword(passwordEncoder.encode("password"));
        userToSave.setRole("ADMIN");
        doThrow(DataIntegrityViolationException.class).when(userRepository).saveAndFlush(userToSave);

        // Verify the result:
        assertThrows(IllegalArgumentException.class, () -> userService.addAdmin(userDto), "Should throw IllegalArgumentException.class");
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
        assertEquals("email@test.com", result.getUsername(), "Username should be email@test.com");
        assertEquals("123 Test St", result.getAddress(), "Address should be 123 Test St");
        assertEquals("123-456-7890", result.getPhoneNumber(), "Phone number should be 123-456-7890");
        assertEquals("123-45-6789", result.getSsn(), "SSN should be 123-45-6789");
    }

    // Login success:
    @Test
    public void loginSuccessTest() {

        UserDto credentials = new UserDto();
        credentials.setEmail("email@test.com");
        credentials.setPassword("password");

        // Define Stubbing:
        when(userRepository.findByUsername("email@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

        // Call the method to test:
        UserDto result = userService.login(credentials);

        // Verify the results:
        assertEquals("email@test.com", result.getUsername(), "Username should be email@test.com");
    }

    // Login Invalid Username:
    @Test
    public void loginInvalidCredentialsTest() {

        UserDto credentials = new UserDto();
        credentials.setEmail("email@test.com");
        credentials.setPassword("password");

        // Define Stubbing:
        when(userRepository.findByUsername("email@test.com")).thenReturn(Optional.empty());

        // Verify the result:
        assertThrows(IllegalArgumentException.class, () -> userService.login(credentials), "Should throw IllegalArgumentException");
    }

    // Login Invalid Password:
    @Test
    public void loginInvalidPasswordTest() {

        UserDto credentials = new UserDto();
        credentials.setEmail("email@test.com");
        credentials.setPassword("password");

        // Define Stubbing:
        when(userRepository.findByUsername("email@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(false);

        // Verify the result:
        assertThrows(IllegalArgumentException.class, () -> userService.login(credentials), "Should throw IllegalArgumentException");
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
        when(userRepository.findByUsername("email@test.com")).thenReturn(Optional.of(user));

        // Call method to test:
        UserDetails result = userService.loadUserByUsername(user.getUsername());

        // Verify result:
        assertEquals("email@test.com", result.getUsername(), "Username should be: email@test.com");
    }

    // Load User by Username User Not Found:
    @Test
    public void loadUserByUsernameThrowsUsernameNotFoundTest() {

        // Define stubbing:
        when(userRepository.findByUsername("email@test.com")).thenReturn(Optional.empty());

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
        assertEquals("email@test.com", result.getUsername(), "Username should be email@test.com");
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

    // Find all Credits claimed by a User for a given year:
    @Test
    public void findAllCreditsByUserIdTest() {

        // Define stubbing:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userCreditRepository.findAllByUserIdAndYear(1, 2024)).thenReturn(List.of(userCredit));

        // Call method to test:
        List<UserCreditDto> result = userService.findAllCreditsByUserIdAndYear(1, 2024);

        // Verify the result:
        assertEquals(1, result.size(), "Should return a list of size: 1");
    }

    // Remove Tax Credit from User Success:
    @Test
    public void removeTaxCreditSuccessTest() {

        // Define Stubbings:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userCreditRepository.findById(1)).thenReturn(Optional.of(userCredit));

        // Call the method to test:
        userService.removeTaxCredit(1, 1);

        // Verify result:
        verify(userCreditRepository).deleteById(1);
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

    // Find all Deductions claimed by a User for a given year:
    @Test
    public void findAllDeductionsByUserIdAndYearTest() {

        // Define stubbing:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userDeductionRepository.findAllByUserIdAndYear(1, 2024)).thenReturn(List.of(userDeduction));

        // Call method to test:
        List<UserDeductionDto> result = userService.findAllDeductionsByUserIdAndYear(1, 2024);

        // Verify the result:
        assertEquals(1, result.size(), "Should return a list of size: 1");
    }

    // Remove Tax Deduction from User Success:
    @Test
    public void removeTaxDeductionSuccessTest() {

        // Define Stubbings:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userDeductionRepository.findById(1)).thenReturn(Optional.of(userDeduction));

        // Call the method to test:
        userService.removeDeduction(1, 1);

        // Verify result:
        verify(userDeductionRepository).deleteById(1);
    }
}

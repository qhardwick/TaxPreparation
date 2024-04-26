package com.skillstorm.services;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.entities.User;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.repositories.UserCreditRepository;
import com.skillstorm.repositories.UserDeductionRepository;
import com.skillstorm.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks private static UserServiceImpl userService;
    @Mock private static UserRepository userRepository;
    @Mock private static UserCreditRepository userCreditRepository;
    @Mock private static UserDeductionRepository userDeductionRepository;
    @Mock private static RestTemplate restTemplate;
    private static String creditsUrl;
    private static String deductionsUrl;
    @Spy private static Environment environment;

    private static UserDto userDto;
    private static User user;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, userCreditRepository, userDeductionRepository, restTemplate, creditsUrl, deductionsUrl, environment);
        ReflectionTestUtils.setField(userService, "creditsUrl", "http://localhost:8080/taxstorm/credits");
        ReflectionTestUtils.setField(userService, "deductionsUrl", "http://localhost:8080/taxstorm/deductions");

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
        user.setPassword("password");
        user.setAddress("123 Test St");
        user.setPhoneNumber("123-456-7890");
        user.setSsn("123-45-6789");
    }

    // Add new User:
    @Test
    public void addUserTest() {

        // Define Stubbing:
        when(userRepository.saveAndFlush(userDto.getUser())).thenReturn(user);

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

    // Find User by Username Success:
    @Test
    public void findUserByUsernameSuccessTest() {

        // Define Stubbing:
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));

        // Call the method to test:
        UserDto result = userService.findUserByUsername("testuser");

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

    // Find User by Username Not Found:
    @Test
    public void findUserByUsernameNotFoundTest() {

        // Define Stubbing:
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Call the method to test:
        assertThrows(UserNotFoundException.class, () -> userService.findUserByUsername("testuser"), "UserNotFoundException should be thrown");
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
    //@Test
    public void addTaxCreditSuccessTest() {

        // Define Stubbings:
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Call the method to test:
    }
}

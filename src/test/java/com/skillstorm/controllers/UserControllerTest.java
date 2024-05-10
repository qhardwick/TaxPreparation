package com.skillstorm.controllers;

import com.skillstorm.dtos.UserCreditDto;
import com.skillstorm.dtos.UserDeductionDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.services.UserService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks private static UserController userController;
    @Mock private static UserService userService;
    private static Validator validator;

    private static UserDto newUser;
    private static UserDto returnedNewUser;
    private static UserDto updatedUser;
    private static UserDto returnedUpdatedUser;

    private static UserCreditDto userCreditDto;
    private static UserCreditDto returnedUserCreditDto;
    private static List<UserCreditDto> userCreditDtoList;

    private static UserDeductionDto userDeductionDto;
    private static UserDeductionDto returnedUserDeductionDto;
    private static List<UserDeductionDto> userDeductionDtoList;

    @BeforeEach
    public void setup() {
        userController = new UserController(userService);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        setupUsers();
        setUpCredits();
        setUpDeductions();
    }

    private void setUpDeductions() {

            userDeductionDto = new UserDeductionDto();
            userDeductionDto.setYear(2024);
            userDeductionDto.setUserId(1);
            userDeductionDto.setDeductionId(1);
            userDeductionDto.setAmountSpent(BigDecimal.valueOf(1000));

            returnedUserDeductionDto = new UserDeductionDto();
            returnedUserDeductionDto.setId(1);
            returnedUserDeductionDto.setYear(2024);
            returnedUserDeductionDto.setUserId(1);
            returnedUserDeductionDto.setDeductionId(1);
            returnedUserDeductionDto.setAmountSpent(BigDecimal.valueOf(1000));
            returnedUserDeductionDto.setDeductionAmount(BigDecimal.valueOf(500));

            userCreditDtoList = List.of(returnedUserCreditDto);
            userDeductionDtoList = List.of(returnedUserDeductionDto);
    }

    private void setUpCredits() {

        userCreditDto = new UserCreditDto();
        userCreditDto.setYear(2024);
        userCreditDto.setUserId(1);
        userCreditDto.setCreditId(1);
        userCreditDto.setCreditsClaimed(1);

        returnedUserCreditDto = new UserCreditDto();
        returnedUserCreditDto.setId(1);
        returnedUserCreditDto.setYear(2024);
        returnedUserCreditDto.setUserId(1);
        returnedUserCreditDto.setCreditId(1);
        returnedUserCreditDto.setCreditsClaimed(1);
        returnedUserCreditDto.setTotalValue(BigDecimal.valueOf(1000));
    }

    private void setupUsers() {

        newUser = new UserDto();
        newUser.setEmail("test@email.com");
        newUser.setPassword("testPassword");

        returnedNewUser = new UserDto();
        returnedNewUser.setId(1);
        returnedNewUser.setEmail("test@email.com");
        returnedNewUser.setUsername("test@email.com");

        updatedUser = new UserDto();
        updatedUser.setId(1);
        updatedUser.setFirstName("testFirstName");
        updatedUser.setLastName("testLastName");
        updatedUser.setEmail("test@email.com");
        updatedUser.setUsername("test@email.com");
        updatedUser.setAddress("123 Test St");
        updatedUser.setPhoneNumber("555-555-5555");
        updatedUser.setSsn("123-45-6789");

        returnedUpdatedUser = new UserDto();
        returnedUpdatedUser.setId(1);
        returnedUpdatedUser.setFirstName("testFirstName");
        returnedUpdatedUser.setLastName("testLastName");
        returnedUpdatedUser.setEmail("test@email.com");
        returnedUpdatedUser.setUsername("test@email.com");
        returnedUpdatedUser.setAddress("123 Test St");
        returnedUpdatedUser.setPhoneNumber("555-555-5555");
        returnedUpdatedUser.setSsn("123-45-6789");
    }

    // Test Add new User:
    @Test
    public void addNewUserTest() {

        // Define stubbing:
        when(userService.addUser(newUser)).thenReturn(returnedNewUser);

        // Call method to test:
        ResponseEntity<UserDto> result =  userController.addUser(newUser);

        // Verify result:
        assertEquals(201, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.CREATED, result.getStatusCode(), "Response should be CREATED");
        assertEquals("/1", result.getHeaders().get("Location").get(0), "Should create URI: '/1");
        assertEquals(1, result.getBody().getId(), "User ID should be: 1");
        assertEquals("test@email.com", result.getBody().getEmail(), "Email should be: test@email.com");
        assertEquals("test@email.com", result.getBody().getUsername(), "Username should be: test@email.com");
        assertNull(result.getBody().getPassword(), "Returned object password should be: null");

    }

    // Test Add new Admin:
    @Test
    public void addNewAdminTest() {

        returnedNewUser.setRole("ADMIN");

        // Define stubbing:
        when(userService.addAdmin(newUser)).thenReturn(returnedNewUser);

        // Call method to test:
        ResponseEntity<UserDto> result = userController.addAdmin(newUser);

        // Verify result:
        assertEquals(201, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.CREATED, result.getStatusCode(), "Response should be CREATED");
        assertEquals("/1", result.getHeaders().get("Location").get(0), "Should create URI: '/1");
        assertEquals(1, result.getBody().getId(), "User ID should be: 1");
        assertEquals("test@email.com", result.getBody().getEmail(), "Email should be: test@email.com");
        assertEquals("test@email.com", result.getBody().getUsername(), "Username should be: test@email.com");
        assertEquals("ADMIN", result.getBody().getRole(), "Role should be: ADMIN");
    }

    // Test Find User by ID:
    @Test
    public void findUserByIdTest() {

        // Define stubbing:
        when(userService.findUserById(1)).thenReturn(returnedNewUser);

        // Call method to test:
        ResponseEntity<UserDto> result = userController.findUserById(1);

        // Verify result:
        assertEquals(200, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should be OK");
        assertEquals(1, result.getBody().getId(), "User ID should be: 1");
        assertEquals("test@email.com", result.getBody().getEmail(), "Email should be: test@email.com");
        assertEquals("test@email.com", result.getBody().getUsername(), "Username should be: test@email.com");
        assertNull(result.getBody().getPassword(), "Returned object password should be: null");
    }

    // Test Login:
    @Test
    public void loginTest() {

        // Define stubbing:
        when(userService.login(newUser)).thenReturn(returnedNewUser);

        // Call method to test:
        ResponseEntity<UserDto> result =  userController.login(newUser);

        // Verify result:
        assertEquals(200, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should be OK");
        assertEquals(1, result.getBody().getId(), "User ID should be: 1");
        assertEquals("test@email.com", result.getBody().getEmail(), "Email should be: test@email.com");
        assertEquals("test@email.com", result.getBody().getUsername(), "Username should be: test@email.com");
        assertNull(result.getBody().getPassword(), "Returned object password should be: null");
    }

    // Test Update User:
    @Test
    public void updateUserTest() {

        // Define stubbing:
        when(userService.updateUserById(1, updatedUser)).thenReturn(returnedUpdatedUser);

        // Call method to test:
        ResponseEntity<UserDto> result =  userController.updateUserById(1, updatedUser);

        // Verify result:
        assertEquals(200, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should be OK");
        assertEquals(1, result.getBody().getId(), "User ID should be: 1");
        assertEquals("testFirstName", result.getBody().getFirstName(), "First name should be: testFirstName");
        assertEquals("testLastName", result.getBody().getLastName(), "Last name should be: testLastName");
        assertEquals("test@email.com", result.getBody().getEmail(), "Email should be: test@email.com");
        assertEquals("test@email.com", result.getBody().getUsername(), "Username should be: test@email.com");
        assertEquals("123 Test St", result.getBody().getAddress(), "Address should be: 123 Test St");
        assertEquals("555-555-5555", result.getBody().getPhoneNumber(), "Phone number should be: 555-555-5555");
        assertEquals("123-45-6789", result.getBody().getSsn(), "SSN should be: 123-45-6789");
        assertNull(result.getBody().getPassword(), "Returned object password should be: null");

    }

    // Test Update Password:
    @Test
    public void updatePasswordTest() {

        // Call method to test:
        ResponseEntity<Void> result =  userController.updatePasswordById(1, "newPassword");

        // Verify that the service method was called with the correct arguments:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(userService).updatePasswordById(idCaptor.capture(), passwordCaptor.capture());

        // Verify result:
        assertEquals(200, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should be OK");
        assertNull(result.getBody(), "Response body should be: null");
        assertEquals(1, idCaptor.getValue(), "ID should be: 1");
        assertEquals("newPassword", passwordCaptor.getValue(), "Password should be: newPassword");
    }

    // Test Delete User:
    @Test
    public void deleteUserTest() {

        // Call method to test:
        ResponseEntity<Void> result =  userController.deleteUserById(1);

        // Verify that the service method was called with the correct arguments:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(userService).deleteUserById(idCaptor.capture());

        // Verify result:
        assertEquals(204, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode(), "Response should be NO_CONTENT");
        assertNull(result.getBody(), "Response body should be: null");
        assertEquals(1, idCaptor.getValue(), "ID should be: 1");
    }

    // Test Add Tax Credit:
    @Test
    public void addTaxCreditTest() {

        // Define stubbing:
        when(userService.addTaxCredit(1, userCreditDto)).thenReturn(returnedUserCreditDto);

        // Call method to test:
        ResponseEntity<UserCreditDto> result =  userController.addTaxCredit(1, userCreditDto);

        // Verify result:
        assertEquals(200, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should be OK");
        assertEquals(1, result.getBody().getId(), "UserCredit ID should be: 1");
        assertEquals(2024, result.getBody().getYear(), "Year should be: 2024");
        assertEquals(1, result.getBody().getUserId(), "User ID should be: 1");
        assertEquals(1, result.getBody().getCreditId(), "Credit ID should be: 1");
        assertEquals(1, result.getBody().getCreditsClaimed(), "Credits claimed should be: 1");
        assertEquals(BigDecimal.valueOf(1000), result.getBody().getTotalValue(), "Total value should be: 1000");
    }

    // Test Find All User Credits by User ID:
    @Test
    public void findAllCreditsByUserIdTest() {

        // Define stubbing:
        when(userService.findAllCreditsByUserIdAndYear(1, 2024)).thenReturn(userCreditDtoList);

        // Call method to test:
        ResponseEntity<List<UserCreditDto>> result =  userController.findAllCreditsByUserId(1, 2024);

        // Verify result:
        assertEquals(200, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should be OK");
        assertEquals(1, result.getBody().get(0).getId(), "UserCredit ID should be: 1");
        assertEquals(2024, result.getBody().get(0).getYear(), "Year should be: 2024");
        assertEquals(1, result.getBody().get(0).getUserId(), "User ID should be: 1");
        assertEquals(1, result.getBody().get(0).getCreditId(), "Credit ID should be: 1");
        assertEquals(1, result.getBody().get(0).getCreditsClaimed(), "Credits claimed should be: 1");
        assertEquals(BigDecimal.valueOf(1000), result.getBody().get(0).getTotalValue(), "Total value should be: 1000");
    }

    // Test Remove Tax Credit:
    @Test
    public void removeTaxCreditTest() {

        // Call method to test:
        ResponseEntity<Void> result =  userController.removeTaxCredit(1, 1);

        // Verify that the service method was called with the correct arguments:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> creditIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(userService).removeTaxCredit(idCaptor.capture(), creditIdCaptor.capture());

        // Verify result:
        assertEquals(204, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode(), "Response should be NO_CONTENT");
        assertNull(result.getBody(), "Response body should be: null");
        assertEquals(1, idCaptor.getValue(), "ID should be: 1");
        assertEquals(1, creditIdCaptor.getValue(), "Credit ID should be: 1");
    }

    // Test Add Deduction:
    @Test
    public void addDeductionTest() {

        // Define stubbing:
        when(userService.addDeduction(1, userDeductionDto)).thenReturn(returnedUserDeductionDto);

        // Call method to test:
        ResponseEntity<UserDeductionDto> result =  userController.addDeduction(1, userDeductionDto);

        // Verify result:
        assertEquals(200, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should be OK");
        assertEquals(1, result.getBody().getId(), "UserDeduction ID should be: 1");
        assertEquals(2024, result.getBody().getYear(), "Year should be: 2024");
        assertEquals(1, result.getBody().getUserId(), "User ID should be: 1");
        assertEquals(1, result.getBody().getDeductionId(), "Deduction ID should be: 1");
        assertEquals(BigDecimal.valueOf(1000), result.getBody().getAmountSpent(), "Amount spent should be: 1000");
        assertEquals(BigDecimal.valueOf(500), result.getBody().getDeductionAmount(), "Deduction amount should be: 500");
    }

    // Test Find All User Deductions by User ID:
    @Test
    public void findAllDeductionsByUserIdTest() {

        // Define stubbing:
        when(userService.findAllDeductionsByUserIdAndYear(1, 2024)).thenReturn(userDeductionDtoList);

        // Call method to test:
        ResponseEntity<List<UserDeductionDto>> result =  userController.findAllDeductionsByUserIdAndYear(1, 2024);

        // Verify result:
        assertEquals(200, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should be OK");
        assertEquals(1, result.getBody().get(0).getId(), "UserDeduction ID should be: 1");
        assertEquals(2024, result.getBody().get(0).getYear(), "Year should be: 2024");
        assertEquals(1, result.getBody().get(0).getUserId(), "User ID should be: 1");
        assertEquals(1, result.getBody().get(0).getDeductionId(), "Deduction ID should be: 1");
        assertEquals(BigDecimal.valueOf(1000), result.getBody().get(0).getAmountSpent(), "Amount spent should be: 1000");
        assertEquals(BigDecimal.valueOf(500), result.getBody().get(0).getDeductionAmount(), "Deduction amount should be: 500");
    }

    // Test Remove Deduction:
    @Test
    public void removeDeductionTest() {

        // Call method to test:
        ResponseEntity<Void> result =  userController.removeDeduction(1, 1);

        // Verify that the service method was called with the correct arguments:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> deductionIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(userService).removeDeduction(idCaptor.capture(), deductionIdCaptor.capture());

        // Verify result:
        assertEquals(204, result.getStatusCode().value(), "Response should be: 201");
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode(), "Response should be NO_CONTENT");
        assertNull(result.getBody(), "Response body should be: null");
        assertEquals(1, idCaptor.getValue(), "ID should be: 1");
        assertEquals(1, deductionIdCaptor.getValue(), "Deduction ID should be: 1");
    }
}

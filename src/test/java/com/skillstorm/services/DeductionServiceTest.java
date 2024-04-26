package com.skillstorm.services;

import com.skillstorm.dtos.DeductionDto;
import com.skillstorm.entities.Deduction;
import com.skillstorm.exceptions.DeductionNotFoundException;
import com.skillstorm.repositories.DeductionRepository;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeductionServiceTest {

    @InjectMocks
    private static DeductionServiceImpl deductionService;
    @Mock
    private static DeductionRepository deductionRepository;
    @Spy
    private static Environment environment;

    private static Deduction deduction;
    private static DeductionDto deductionDto;

    @BeforeEach
    public void setUp() {

    deductionService = new DeductionServiceImpl(deductionRepository, environment);

    deductionDto =new DeductionDto();
    deductionDto.setName("Test Deduction");
    deductionDto.setRate(BigDecimal.valueOf(0.5));

    deduction =new Deduction();
    deduction.setId(1);
    deduction.setName("Test Deduction");
    deduction.setRate(BigDecimal.valueOf(0.5));
    }

    @AfterEach
    public void resetMocks() {
        reset(deductionRepository, environment);
    }

    // Add Deduction:
    @Test
    public void addDeductionTest() {

        //Define stubbing:
        when(deductionRepository.saveAndFlush(deductionDto.getDeduction())).thenReturn(deduction);

        //Call the method:
        DeductionDto result = deductionService.addDeduction(deductionDto);

        //Verify the result:
        assertEquals(1, result.getId(), "Deduction ID should be 1");
        assertEquals("Test Deduction", result.getName(), "Deduction name should be 'Test Deduction'");
        assertEquals(BigDecimal.valueOf(0.5), result.getRate(), "Deduction rate should be 0.5");
    }

    // Find Deduction by ID Success:
    @Test
    public void findDeductionByIdSuccessTest() {

        //Define stubbing:
        when(deductionRepository.findById(1)).thenReturn(java.util.Optional.of(deduction));

        //Call the method:
        DeductionDto result = deductionService.findDeductionById(1);

        //Verify the result:
        assertEquals(1, result.getId(), "Deduction ID should be 1");
        assertEquals("Test Deduction", result.getName(), "Deduction name should be 'Test Deduction'");
        assertEquals(BigDecimal.valueOf(0.5), result.getRate(), "Deduction rate should be 0.5");
    }

    // Find Deduction by ID Not Exists:
    @Test
    public void findDeductionByIdNotExistsTest() {

        //Define stubbing:
        when(deductionRepository.findById(1)).thenReturn(java.util.Optional.empty());

        //Execute the method and verify the result:
        assertThrows(DeductionNotFoundException.class, () -> deductionService.findDeductionById(1), "DeductionNotFoundException should be thrown");
    }

    // Find All Deductions:
    @Test
    public void findAllDeductionsTest() {

        //Define stubbing:
        when(deductionRepository.findAll()).thenReturn(java.util.List.of(deduction));

        //Call the method:
        java.util.List<DeductionDto> result = deductionService.findAllDeductions();

        //Verify the result:
        assertEquals(1, result.get(0).getId(), "Deduction ID should be 1");
        assertEquals("Test Deduction", result.get(0).getName(), "Deduction name should be 'Test Deduction'");
        assertEquals(BigDecimal.valueOf(0.5), result.get(0).getRate(), "Deduction rate should be 0.5");
    }

    // Update Deduction by ID:
    @Test
    public void updateDeductionByIdTest() {

        //Define stubbing:
        when(deductionRepository.findById(1)).thenReturn(java.util.Optional.of(deduction));
        when(deductionRepository.saveAndFlush(deduction)).thenReturn(deduction);

        //Call the method:
        DeductionDto result = deductionService.updateDeductionById(1, deductionDto);

        //Verify the result:
        assertEquals(1, result.getId(), "Deduction ID should be 1");
        assertEquals("Test Deduction", result.getName(), "Deduction name should be 'Test Deduction'");
        assertEquals(BigDecimal.valueOf(0.5), result.getRate(), "Deduction rate should be 0.5");
    }

    // Delete Deduction by ID:
    @Test
    public void deleteDeductionByIdTest() {

        //Define stubbing:
        when(deductionRepository.findById(1)).thenReturn(java.util.Optional.of(deduction));

        //Define ArgumentCaptor:
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

        //Call the method:
        deductionService.deleteDeductionById(1);

        //Verify the method call:
        verify(deductionRepository).deleteById(argumentCaptor.capture());

        //Verify the result:
        assertEquals(1, argumentCaptor.getValue(), "Deduction ID should be 1");
    }
}

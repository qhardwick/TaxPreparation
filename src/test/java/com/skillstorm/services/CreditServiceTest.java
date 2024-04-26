package com.skillstorm.services;

import com.skillstorm.dtos.CreditDto;
import com.skillstorm.entities.Credit;
import com.skillstorm.exceptions.CreditNotFoundException;
import com.skillstorm.repositories.CreditRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @InjectMocks private static CreditServiceImpl creditService;
    @Mock private static CreditRepository creditRepository;
    @Spy private static Environment environment;

    private static Credit credit;
    private static CreditDto creditDto;

    @BeforeEach
    public void setUp() {

        creditService = new CreditServiceImpl(creditRepository, environment);

        creditDto = new CreditDto();
        creditDto.setName("Test Credit");
        creditDto.setValue(BigDecimal.valueOf(1000.00));

        credit = new Credit();
        credit.setId(1);
        credit.setName("Test Credit");
        credit.setValue(BigDecimal.valueOf(1000.00));
    }

    @AfterEach
    public void resetMocks() {
        reset(creditRepository, environment);
    }

    // Add Credit:
    @Test
    public void addCreditTest() {

        //Define stubbing:
        when(creditRepository.saveAndFlush(creditDto.getCredit())).thenReturn(credit);

        //Execute the method and save the result:
        CreditDto result = creditService.addCredit(creditDto);

        //Verify the result:
        assertEquals(1, result.getId(), "Credit ID should be 1");
        assertEquals("Test Credit", result.getName(), "Credit name should be 'Test Credit'");
        assertEquals(BigDecimal.valueOf(1000.00), result.getValue(), "Credit value should be 1000.00");
    }

    // Get Credit By Id and Succeed:
    @Test
    @SneakyThrows
    public void findCreditByIdSuccessTest() {

        //Define stubbing:
        when(creditRepository.findById(1)).thenReturn(Optional.of(credit));

        //Execute the method and save the result:
        CreditDto result = creditService.findCreditById(1);

        //Verify the result:
        assertEquals(1, result.getId(), "Credit ID should be 1");
        assertEquals("Test Credit", result.getName(), "Credit name should be 'Test Credit'");
        assertEquals(BigDecimal.valueOf(1000.00), result.getValue(), "Credit value should be 1000.00");
    }

    // Get Credit By Id Not Exists:
    @Test
    public void findCreditByIdNotFoundTest() {
        //Define stubbing:
        when(creditRepository.findById(2)).thenReturn(Optional.empty());

        //Execute the method and verify the result:
        assertThrows(CreditNotFoundException.class, () -> creditService.findCreditById(2), "CreditNotFoundException should be thrown");
    }

    // Find All Credits:
    @Test
    public void findAllCreditsTest() {

        //Define stubbing:
        when(creditRepository.findAll()).thenReturn(List.of(credit));

        //Execute the method and save the result:
        List<CreditDto> result = creditService.findAllCredits();

        //Verify the result:
        assertEquals(1, result.get(0).getId(), "Credit ID should be 1");
        assertEquals("Test Credit", result.get(0).getName(), "Credit name should be 'Test Credit'");
        assertEquals(BigDecimal.valueOf(1000.00), result.get(0).getValue(), "Credit value should be 1000.00");
    }

    // Update Credit By Id:
    @Test
    public void updateCreditByIdTest() {

        //Define stubbing:
        when(creditRepository.findById(1)).thenReturn(Optional.of(credit));
        when(creditRepository.saveAndFlush(credit)).thenReturn(credit);

        //Execute the method and save the result:
        CreditDto result = creditService.updateCreditById(1, creditDto);

        //Verify the result:
        assertEquals(1, result.getId(), "Credit ID should be 1");
        assertEquals("Test Credit", result.getName(), "Credit name should be 'Test Credit'");
        assertEquals(BigDecimal.valueOf(1000.00), result.getValue(), "Credit value should be 1000.00");
    }

    // Delete Credit By Id:
    @Test
    public void deleteCreditByIdTest() {

        //Define stubbing:
        when(creditRepository.findById(1)).thenReturn(Optional.of(credit));

        //Define ArgumentCaptor:
        ArgumentCaptor<Integer> creditCaptor = ArgumentCaptor.forClass(Integer.class);

        //Execute the method:
        creditService.deleteCreditById(1);

        //Verify the method was called:
        verify(creditRepository).deleteById(creditCaptor.capture());

        //Verify the result:
        assertEquals(1, creditCaptor.getValue(), "Credit ID should be 1") ;
    }
}

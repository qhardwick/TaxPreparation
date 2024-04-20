package com.skillstorm.services;

import com.skillstorm.dtos.CreditDto;
import com.skillstorm.entities.Credit;
import com.skillstorm.exceptions.CreditNotFoundException;
import com.skillstorm.repositories.CreditRepository;
import com.skillstorm.utils.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PropertySource("classpath:application.properties")
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final Environment environment;

    @Autowired
    public CreditServiceImpl(CreditRepository creditRepository, Environment environment) {
        this.creditRepository = creditRepository;
        this.environment = environment;
    }

    // Add Credit:
    @Override
    public CreditDto addCredit(CreditDto credit) {
        return new CreditDto(creditRepository.saveAndFlush(credit.getCredit()));
    }

    // Get Credit By Id:
    @Override
    public CreditDto findCreditById(int id) {
        Optional<Credit> creditOptional = creditRepository.findById(id);
        if(!creditOptional.isPresent()) {
            throw new CreditNotFoundException(environment.getProperty(SystemMessages.CREDIT_NOT_FOUND.toString()));
        }
        return new CreditDto(creditOptional.get());
    }

    // Update Credit By Id:
    @Override
    public CreditDto updateCreditById(int id, CreditDto updatedCredit) {
        findCreditById(id);
        updatedCredit.setId(id);
        return new CreditDto(creditRepository.saveAndFlush(updatedCredit.getCredit()));
    }

    // Delete Credit By Id:
    @Override
    public void deleteCreditById(int id) {
        findCreditById(id);
        creditRepository.deleteById(id);
    }
}

package com.skillstorm.services;

import com.skillstorm.dtos.DeductionDto;
import com.skillstorm.entities.Deduction;
import com.skillstorm.exceptions.DeductionNotFoundException;
import com.skillstorm.repositories.DeductionRepository;
import com.skillstorm.utils.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:application.properties")
public class DeductionServiceImpl implements DeductionService {

    private final DeductionRepository deductionRepository;
    private final Environment environment;

    @Autowired
    public DeductionServiceImpl(DeductionRepository deductionRepository, Environment environment) {
        this.deductionRepository = deductionRepository;
        this.environment = environment;
    }

    // Add new Deduction:
    @Override
    public DeductionDto addDeduction(DeductionDto newDeduction) {
        return new DeductionDto(deductionRepository.save(newDeduction.getDeduction()));
    }

    // Find Deduction by ID:
    @Override
    public DeductionDto findDeductionById(int id) {
        Optional<Deduction> deductionOptional = deductionRepository.findById(id);
        if(!deductionOptional.isPresent()) {
            throw new DeductionNotFoundException(environment.getProperty(SystemMessages.DEDUCTION_NOT_FOUND.toString()));
        }
        return new DeductionDto(deductionOptional.get());
    }

    // Find All Deductions:
    @Override
    public List<DeductionDto> findAllDeductions() {
        return deductionRepository.findAll().stream().map(DeductionDto::new).toList();
    }

    // Update Deduction by ID:
    @Override
    public DeductionDto updateDeductionById(int id, DeductionDto updatedDeduction) {
        findDeductionById(id);
        updatedDeduction.setId(id);
        return new DeductionDto(deductionRepository.save(updatedDeduction.getDeduction()));
    }

    // Delete Deduction by ID:
    @Override
    public void deleteDeductionById(int id) {
        findDeductionById(id);
        deductionRepository.deleteById(id);
    }
}

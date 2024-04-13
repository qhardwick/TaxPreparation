package com.skillstorm.controllers;

import com.skillstorm.dtos.DeductionDto;
import com.skillstorm.services.DeductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deductions")
public class DeductionController {

    private final DeductionService deductionService;

    @Autowired
    public DeductionController(DeductionService deductionService) {
        this.deductionService = deductionService;
    }

    // Test Endpoint:
    @RequestMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    // Add new Deduction:
    @PostMapping
    public ResponseEntity<DeductionDto> addDeduction(@RequestBody DeductionDto newDeduction) {
        return ResponseEntity.ok(deductionService.addDeduction(newDeduction));
    }

    // Find Deduction by ID:
    @GetMapping("/{id}")
    public ResponseEntity<DeductionDto> findDeductionById(@PathVariable int id) {
        return ResponseEntity.ok(deductionService.findDeductionById(id));
    }

    // Find All Deductions:
    @GetMapping
    public ResponseEntity<List<DeductionDto>> findAllDeductions() {
        return ResponseEntity.ok(deductionService.findAllDeductions());
    }

    // Update Deduction by ID:
    @PutMapping("/{id}")
    public ResponseEntity<DeductionDto> updateDeductionById(@PathVariable int id, @RequestBody DeductionDto updatedDeduction) {
        return ResponseEntity.ok(deductionService.updateDeductionById(id, updatedDeduction));
    }

    // Delete Deduction by ID:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeductionById(@PathVariable int id) {
        deductionService.deleteDeductionById(id);
        return ResponseEntity.noContent().build();
    }
}

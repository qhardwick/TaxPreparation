package com.skillstorm.controllers;

import com.skillstorm.dtos.TaxFormDto;
import com.skillstorm.services.TaxFormService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/taxforms")
public class TaxFormController {

    private final TaxFormService taxFormService;

    @Autowired
    public TaxFormController(TaxFormService taxFormService) {
        this.taxFormService = taxFormService;
    }

    // Test Endpoint:
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    // Add new TaxForm:
    @PostMapping
    public ResponseEntity<TaxFormDto> addTaxForm(@Valid @RequestBody TaxFormDto newTaxForm) {
        TaxFormDto createdTaxForm = taxFormService.addTaxForm(newTaxForm);
        return ResponseEntity.created(URI.create("/" + createdTaxForm.getId())).body(createdTaxForm);
    }

    // Find TaxForm by ID:
    @GetMapping("/{id}")
    public ResponseEntity<TaxFormDto> findTaxFormById(@PathVariable int id) {
        return ResponseEntity.ok(taxFormService.findTaxFormById(id));
    }

    // Update TaxForm by ID:
    @PutMapping("/{id}")
    public ResponseEntity<TaxFormDto> updateTaxFormById(@PathVariable int id, @Valid @RequestBody TaxFormDto updatedTaxForm) {
        return ResponseEntity.ok(taxFormService.updateTaxFormById(id, updatedTaxForm));
    }

    // Delete TaxForm by ID:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxFormById(@PathVariable int id) {
        taxFormService.deleteTaxFormById(id);
        return ResponseEntity.noContent().build();
    }
}

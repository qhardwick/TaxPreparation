package com.skillstorm.controllers;

import com.skillstorm.dtos.TaxFormDto;
import com.skillstorm.services.TaxFormService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/taxforms")
@CrossOrigin(origins="*")
public class TaxFormController {

    private final TaxFormService taxFormService;

    @Autowired
    public TaxFormController(TaxFormService taxFormService) {
        this.taxFormService = taxFormService;
    }

    // Submit TaxForm:
    @PostMapping("/{userId}/{year}")
    public ResponseEntity<TaxFormDto> submitTaxForm(@PathVariable("userId") int userId, @PathVariable("year") int year) {
        TaxFormDto createdTaxForm = taxFormService.submitTaxForm(userId, year);
        return ResponseEntity.created(URI.create("/" + createdTaxForm.getId())).body(createdTaxForm);
    }

    // Find TaxForm by ID:
    @GetMapping("/{id}")
    public ResponseEntity<TaxFormDto> findTaxFormById(@PathVariable int id) {
        return ResponseEntity.ok(taxFormService.findTaxFormById(id));
    }

    // Populate TaxForm based on User ID and Year:
    @GetMapping("/{userId}/{year}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<TaxFormDto> populateTaxFormByUserId(@PathVariable("userId") int userId, @PathVariable("year") int year) {
        return ResponseEntity.ok(taxFormService.populateTaxFormByUserId(userId, year));
    }

    // Find all TaxForms by User ID:
    @GetMapping
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<List<TaxFormDto>> findAllTaxFormsByUserId(@PathParam("userId") int userId) {
        return ResponseEntity.ok(taxFormService.findAllTaxFormsByUserId(userId));
    }

    // Update TaxForm by ID:
    @PutMapping("/{id}")
    public ResponseEntity<TaxFormDto> updateTaxFormById(@PathVariable int id) {
        return ResponseEntity.ok(taxFormService.updateTaxFormById(id));
    }

    // Delete TaxForm by ID:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxFormById(@PathVariable int id) {
        taxFormService.deleteTaxFormById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.skillstorm.controllers;

import com.skillstorm.dtos.CreditDto;
import com.skillstorm.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credits")
@CrossOrigin(origins="*")
public class CreditController {

    private final CreditService creditService;

    @Autowired
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    // Add Credit:
    @PostMapping
    @PreAuthorize("authentication.principal.role == 'ADMIN'")
    public ResponseEntity<CreditDto> addCredit(@RequestBody CreditDto credit) {
        return ResponseEntity.ok(creditService.addCredit(credit));
    }

    // Get Credit By Id:
    @GetMapping("/{id}")
    public ResponseEntity<CreditDto> findCreditById(@PathVariable int id) {
        return ResponseEntity.ok(creditService.findCreditById(id));
    }

    // Get All Credits:
    @GetMapping
    public ResponseEntity<List<CreditDto>> findAllCredits() {
        return ResponseEntity.ok(creditService.findAllCredits());
    }

    // Update Credit By Id:
    @PutMapping("/{id}")
    @PreAuthorize("authentication.principal.role == 'ADMIN'")
    public ResponseEntity<CreditDto> updateCreditById(@PathVariable int id, @RequestBody CreditDto updatedCredit) {
        return ResponseEntity.ok(creditService.updateCreditById(id, updatedCredit));
    }

    // Delete Credit By Id:
    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.principal.role == 'ADMIN'")
    public ResponseEntity<Void> deleteCreditById(@PathVariable int id) {
        creditService.deleteCreditById(id);
        return ResponseEntity.noContent().build();
    }
}

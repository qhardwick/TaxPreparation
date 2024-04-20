package com.skillstorm.controllers;

import com.skillstorm.dtos.CreditDto;
import com.skillstorm.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credits")
public class CreditController {

    private final CreditService creditService;

    // SCA test
    @Autowired
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    // Test Endpoint:
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    // Add Credit:
    @PostMapping
    public ResponseEntity<CreditDto> addCredit(@RequestBody CreditDto credit) {
        return ResponseEntity.ok(creditService.addCredit(credit));
    }

    // Get Credit By Id:
    @GetMapping("/{id}")
    public ResponseEntity<CreditDto> findCreditById(@PathVariable int id) {
        return ResponseEntity.ok(creditService.findCreditById(id));
    }

    // Update Credit By Id:
    @PutMapping("/{id}")
    public ResponseEntity<CreditDto> updateCreditById(@PathVariable int id, @RequestBody CreditDto updatedCredit) {
        return ResponseEntity.ok(creditService.updateCreditById(id, updatedCredit));
    }

    // Delete Credit By Id:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditById(@PathVariable int id) {
        creditService.deleteCreditById(id);
        return ResponseEntity.noContent().build();
    }
}

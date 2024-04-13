package com.skillstorm.controllers;

import com.skillstorm.dtos.W2Dto;
import com.skillstorm.services.W2Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/w2s")
public class W2Controller {

    private final W2Service w2Service;

    @Autowired
    public W2Controller(W2Service w2Service) {
        this.w2Service = w2Service;
    }

    // Test Endpoint:
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    // Add new W2:
    @PostMapping
    public ResponseEntity<W2Dto> addW2(@Valid @RequestBody W2Dto newW2) {
        W2Dto createdW2 = w2Service.addW2(newW2);
        return ResponseEntity.created(URI.create("/" + createdW2.getId())).body(createdW2);
    }

    // Find W2 by ID:
    @GetMapping("/{id}")
    public ResponseEntity<W2Dto> findW2ById(@PathVariable int id) {
        return ResponseEntity.ok(w2Service.findW2ById(id));
    }

    // Update W2 by ID:
    @PutMapping("/{id}")
    public ResponseEntity<W2Dto> updateW2ById(@PathVariable int id, @Valid @RequestBody W2Dto updatedW2) {
        return ResponseEntity.ok(w2Service.updateW2ById(id, updatedW2));
    }

    // Delete W2 by ID:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteW2ById(@PathVariable int id) {
        w2Service.deleteW2ById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.skillstorm.controllers;

import com.skillstorm.dtos.W2Dto;
import com.skillstorm.services.W2Service;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/w2s")
public class W2Controller {

    private final W2Service w2Service;

    @Autowired
    public W2Controller(W2Service w2Service) {
        this.w2Service = w2Service;
    }

    // Add new W2:
    @PostMapping
    @PreAuthorize("#newW2.userId == authentication.principal.id")
    public ResponseEntity<W2Dto> addW2(@Valid @RequestBody W2Dto newW2) {
        W2Dto createdW2 = w2Service.addW2(newW2);
        return ResponseEntity.created(URI.create("/" + createdW2.getId())).body(createdW2);
    }

    // Find W2 by ID:
    @GetMapping("/{id}")
    public ResponseEntity<W2Dto> findW2ById(@PathVariable int id) {
        return ResponseEntity.ok(w2Service.findW2ById(id));
    }

    // Find all by User ID:
    @GetMapping()
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<List<W2Dto>> findW2ByUserId(@PathParam("userId") int userId) {
        return ResponseEntity.ok(w2Service.findW2ByUserId(userId));
    }

    // Update W2 by ID:
    @PutMapping("/{id}")
    @PreAuthorize("#updatedW2.userId == authentication.principal.id")
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

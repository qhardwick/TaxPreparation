package com.skillstorm.controllers;

import com.skillstorm.dtos.W2Dto;
import com.skillstorm.services.W2Service;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    //TODO: Implement a check to ensure that the user deleting the W2 is the same user that created it.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteW2ById(@PathVariable int id) {
        w2Service.deleteW2ById(id);
        return ResponseEntity.noContent().build();
    }

    // Upload image of W2:
    @PostMapping("/{id}/image")
    public ResponseEntity<Void> uploadW2Image(@PathVariable int id, @RequestBody byte[] image, @RequestHeader("Content-Type") String contentType) {
        w2Service.uploadW2Image(id, image, contentType);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Download image of W2:
    @GetMapping(value = "/{id}/image")
    public ResponseEntity<Resource> downloadW2Image(@PathVariable int id) {
        Resource imageResource = w2Service.downloadW2Image(id);
        String contentType = determineContentType(imageResource);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(imageResource);
    }

    // Utility method to determine the content type of a file:
    private String determineContentType(Resource resource) {
        Tika tika = new Tika();
        try {
            // Use Tika to parse the file
            String contentType = tika.detect(resource.getInputStream());
            return contentType;
        } catch (IOException e) {
            // Handle the exception
            throw new RuntimeException("Failed to determine content type.", e);
        }
    }
}

package com.skillstorm.services;

import com.skillstorm.dtos.W2Dto;

public interface W2Service {

    // Add new W2:
    W2Dto addW2(W2Dto newW2);

    // Find W2 by ID:
    W2Dto findW2ById(String id);

    // Update W2 by ID:
    W2Dto updateW2ById(String id, W2Dto updatedW2);

    // Delete W2 by ID:
    void deleteW2ById(String id);
}

package com.skillstorm.services;

import com.skillstorm.dtos.W2Dto;

import java.util.List;

public interface W2Service {

    // Add new W2:
    W2Dto addW2(W2Dto newW2);

    // Find W2 by ID:
    W2Dto findW2ById(int id);

    // Find all by User ID:
    List<W2Dto> findW2ByUserId(int userId);

    // Update W2 by ID:
    W2Dto updateW2ById(int id, W2Dto updatedW2);

    // Delete W2 by ID:
    void deleteW2ById(int id);
}

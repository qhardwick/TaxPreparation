package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
//@Entity
//@Table
//@IdClass(UserW2Id.class)
public class UserW2 {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "w2_id")
    private W2 w2;
}

// Helper class to create a composite primary key
@Data
class UserW2Id implements Serializable {
    private int userId;
    private int w2Id;
}
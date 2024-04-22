package com.skillstorm.entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double value;
}

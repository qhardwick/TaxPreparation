package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "deductions")
public class Deduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(name = "deduction_rate")
    private double rate;
}

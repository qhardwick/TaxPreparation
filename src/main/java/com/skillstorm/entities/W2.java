package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class W2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String employer;

    private int year;

    private double wages;

    private double federalTaxesWithheld;

    private double socialSecurityTaxesWithheld;

    private double medicareTaxesWithheld;

    @Column(name = "user_id")
    private int userId;
}


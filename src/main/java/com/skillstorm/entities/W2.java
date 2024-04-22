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

    // Specifies that we're only interested in the user's id. We don't need the entire user object.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}


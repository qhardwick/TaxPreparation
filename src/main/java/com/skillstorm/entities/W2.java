package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table
public class W2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String employer;

    private int year;

    private BigDecimal wages;

    private BigDecimal federalTaxesWithheld;

    private BigDecimal socialSecurityTaxesWithheld;

    private BigDecimal medicareTaxesWithheld;

    @Column(name = "user_id")
    private int userId;

    // Specifies that we're only interested in the user's id. We don't need the entire user object.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @Override
    public String toString() {
        return "W2{" +
                "id=" + id +
                ", employer='" + employer + '\'' +
                ", year=" + year +
                ", wages=" + wages +
                ", federalTaxesWithheld=" + federalTaxesWithheld +
                ", socialSecurityTaxesWithheld=" + socialSecurityTaxesWithheld +
                ", medicareTaxesWithheld=" + medicareTaxesWithheld +
                ", userId=" + userId +
                '}';
    }
}


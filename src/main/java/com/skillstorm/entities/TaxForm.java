package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tax_forms")
public class TaxForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int year;

    @Column(name = "total_wages")
    private double totalWages;

    @Column(name = "total_federal_taxes_withheld")
    private double totalFederalTaxesWithheld;

    @Column(name = "total_social_security_taxes_withheld")
    private double totalSocialSecurityTaxesWithheld;

    @Column(name = "total_medicare_taxes_withheld")
    private double totalMedicareTaxesWithheld;


    //private List<Deduction> deductions;

    private double refund;
}

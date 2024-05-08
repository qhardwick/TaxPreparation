package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tax_forms")
public class TaxForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int year;

    @Column(name = "total_wages")
    private BigDecimal totalWages;

    @Column(name = "total_federal_taxes_withheld")
    private BigDecimal totalFederalTaxesWithheld;

    @Column(name = "total_social_security_taxes_withheld")
    private BigDecimal totalSocialSecurityTaxesWithheld;

    @Column(name = "total_medicare_taxes_withheld")
    private BigDecimal totalMedicareTaxesWithheld;

    private BigDecimal credits;

    private BigDecimal deductions;

    private BigDecimal refund;
}

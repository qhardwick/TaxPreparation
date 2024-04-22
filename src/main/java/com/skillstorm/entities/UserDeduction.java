package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_deductions")
public class UserDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deduction_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Deduction deduction;

    @Column(name = "amount_spent")
    private double amountSpent;

    @Column(name = "deduction_amount")
    private double deductionAmount;
}

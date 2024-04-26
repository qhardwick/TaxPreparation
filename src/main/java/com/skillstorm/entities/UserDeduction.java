package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "user_deductions")
public class UserDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @Column(name = "deduction_id")
    private int deductionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deduction_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Deduction deduction;

    @Column(name = "amount_spent")
    private BigDecimal amountSpent;

    @Column(name = "deduction_amount")
    private BigDecimal deductionAmount;
}

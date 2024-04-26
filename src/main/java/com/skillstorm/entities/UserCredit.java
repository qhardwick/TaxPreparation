package com.skillstorm.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "user_credits")
public class UserCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @Column(name = "credit_id")
    private int creditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Credit credit;

    @Column(name = "credits_claimed")
    private int creditsClaimed;

    @Column(name = "total_value")
    private BigDecimal totalValue;

}

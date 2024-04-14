package com.skillstorm.dtos;

import com.skillstorm.entities.Credit;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditDto {

    private int id;
    private String name;
    private double amount;

    public CreditDto(Credit credit) {
        this.id = credit.getId();
        this.name = credit.getName();
        this.amount = credit.getAmount();
    }

    public Credit getCredit() {
        Credit credit = new Credit();
        credit.setId(this.id);
        credit.setName(this.name);
        credit.setAmount(this.amount);

        return credit;
    }
}

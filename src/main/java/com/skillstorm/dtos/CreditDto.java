package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.Credit;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditDto {

    private int id;
    private String name;
    private double value;

    public CreditDto(Credit credit) {
        this.id = credit.getId();
        this.name = credit.getName();
        this.value = credit.getValue();
    }

    @JsonIgnore
    public Credit getCredit() {
        Credit credit = new Credit();
        credit.setId(this.id);
        credit.setName(this.name);
        credit.setValue(this.value);

        return credit;
    }
}

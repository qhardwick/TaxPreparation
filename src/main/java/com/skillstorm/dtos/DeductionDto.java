package com.skillstorm.dtos;

import com.skillstorm.entities.Deduction;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeductionDto {

    private int id;
    private String name;
    private double rate;

    public DeductionDto(Deduction deduction) {
        this.id = deduction.getId();
        this.name = deduction.getName();
        this.rate = deduction.getRate();
    }

    public Deduction getDeduction() {
        Deduction deduction = new Deduction();
        deduction.setId(this.id);
        deduction.setName(this.name);
        deduction.setRate(this.rate);

        return deduction;
    }
}

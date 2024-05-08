package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.UserDeduction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class UserDeductionDto {

    private int id;
    private int year;
    private int userId;
    private int deductionId;
    private BigDecimal amountSpent;
    private BigDecimal deductionAmount;

    public UserDeductionDto(UserDeduction userDeduction) {
        this.id = userDeduction.getId();
        this.year = userDeduction.getYear();
        this.userId = userDeduction.getUser().getId();
        this.deductionId = userDeduction.getDeduction().getId();
        this.amountSpent = userDeduction.getAmountSpent();
        this.deductionAmount = userDeduction.getDeductionAmount();
    }

    @JsonIgnore
    public UserDeduction getUserDeduction() {
        UserDeduction userDeduction = new UserDeduction();
        userDeduction.setId(this.id);
        userDeduction.setYear(this.year);
        userDeduction.setUserId(this.userId);
        userDeduction.setDeductionId(this.deductionId);
        userDeduction.setAmountSpent(this.amountSpent);
        userDeduction.setDeductionAmount(this.deductionAmount);

        return userDeduction;
    }
}

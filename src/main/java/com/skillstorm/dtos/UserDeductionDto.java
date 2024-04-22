package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.UserDeduction;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDeductionDto {

    private int id;
    private int userId;
    private int deductionId;
    private double amountSpent;
    private double deductionAmount;

    public UserDeductionDto(UserDeduction userDeduction) {
        this.id = userDeduction.getId();
        this.userId = userDeduction.getUser().getId();
        this.deductionId = userDeduction.getDeduction().getId();
        this.amountSpent = userDeduction.getAmountSpent();
        this.deductionAmount = userDeduction.getDeductionAmount();
    }

    @JsonIgnore
    public UserDeduction getUserDeduction() {
        UserDeduction userDeduction = new UserDeduction();
        userDeduction.setId(this.id);
        userDeduction.setUser(null);
        userDeduction.setDeduction(null);
        userDeduction.setAmountSpent(this.amountSpent);
        userDeduction.setDeductionAmount(this.deductionAmount);

        return userDeduction;
    }
}

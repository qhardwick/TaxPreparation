package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.UserCredit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class UserCreditDto {

    private int id;
    private int userId;
    private int creditId;
    private int creditsClaimed;
    private BigDecimal totalValue;

    public UserCreditDto() {
        // Default value is one so that Tax Credits that can only be claimed once don't need to set the value.
        this.creditsClaimed = 1;
    }

    public UserCreditDto(UserCredit userCredit) {
        this.id = userCredit.getId();
        this.userId = userCredit.getUser().getId();
        this.creditId = userCredit.getCredit().getId();
        this.creditsClaimed = userCredit.getCreditsClaimed();
        this.totalValue = userCredit.getTotalValue();
    }

    @JsonIgnore
    public UserCredit getUserCredit() {
        UserCredit userCredit = new UserCredit();
        userCredit.setId(this.id);
        userCredit.setUserId(this.userId);
        userCredit.setCreditId(this.creditId);
        userCredit.setCreditsClaimed(this.creditsClaimed);
        userCredit.setTotalValue(this.totalValue);

        return userCredit;
    }
}

package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.W2;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class W2Dto {

    private int id;

    @NotEmpty(message = "{w2.employer.must}")
    private String employer;

    @Min(value = 1900, message = "{w2.year.min}")
    private int year;

    @Min(value = 0, message = "{w2.wages.min}")
    private BigDecimal wages;

    @Min(value = 0, message = "{w2.federalTaxesWithheld.min}")
    private BigDecimal federalTaxesWithheld;

    @Min(value = 0, message = "{w2.socialSecurityTaxesWithheld.min}")
    private BigDecimal socialSecurityTaxesWithheld;

    @Min(value = 0, message = "{w2.medicareTaxesWithheld.min}")
    private BigDecimal medicareTaxesWithheld;

    @Min(value = 1, message = "{w2.userId.min}")
    private int userId;

    public W2Dto(W2 w2) {
        this.id = w2.getId();
        this.employer = w2.getEmployer();
        this.year = w2.getYear();
        this.wages = w2.getWages();
        this.federalTaxesWithheld = w2.getFederalTaxesWithheld();
        this.socialSecurityTaxesWithheld = w2.getSocialSecurityTaxesWithheld();
        this.medicareTaxesWithheld = w2.getMedicareTaxesWithheld();
        this.userId = w2.getUserId();
    }

    @JsonIgnore
    public W2 getW2() {
        W2 w2 = new W2();
        w2.setId(this.id);
        w2.setEmployer(this.employer);
        w2.setYear(this.year);
        w2.setWages(this.wages);
        w2.setFederalTaxesWithheld(this.federalTaxesWithheld);
        w2.setSocialSecurityTaxesWithheld(this.socialSecurityTaxesWithheld);
        w2.setMedicareTaxesWithheld(this.medicareTaxesWithheld);
        w2.setUserId(this.getUserId());

        return w2;
    }
}

package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.W2;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class W2Dto {

    private String id;

    private String employer;

    private int year;

    private double wages;

    private double federalTaxesWithheld;

    private double socialSecurityTaxesWithheld;

    private double medicareTaxesWithheld;

    public W2Dto(W2 w2) {
        this.id = w2.getId();
        this.employer = w2.getEmployer();
        this.year = w2.getYear();
        this.wages = w2.getWages();
        this.federalTaxesWithheld = w2.getFederalTaxesWithheld();
        this.socialSecurityTaxesWithheld = w2.getSocialSecurityTaxesWithheld();
        this.medicareTaxesWithheld = w2.getMedicareTaxesWithheld();
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

        return w2;
    }
}

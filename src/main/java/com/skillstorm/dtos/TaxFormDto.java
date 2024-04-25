package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.TaxForm;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaxFormDto {

    private int id;

    private UserDto user;

    private int year;

    private double totalWages;

    private double totalFederalTaxesWithheld;

    private double totalSocialSecurityTaxesWithheld;

    private double totalMedicareTaxesWithheld;

    private double credits;

    private double deductions;

    private double refund;

    public TaxFormDto(TaxForm taxForm) {
        this.id = taxForm.getId();
        this.user = new UserDto(taxForm.getUser());
        this.year = taxForm.getYear();
        this.totalWages = taxForm.getTotalWages();
        this.totalFederalTaxesWithheld = taxForm.getTotalFederalTaxesWithheld();
        this.totalSocialSecurityTaxesWithheld = taxForm.getTotalSocialSecurityTaxesWithheld();
        this.totalMedicareTaxesWithheld = taxForm.getTotalMedicareTaxesWithheld();
        this.credits = taxForm.getCredits();
        this.deductions = taxForm.getDeductions();
        this.refund = taxForm.getRefund();
    }

    @JsonIgnore
    public TaxForm getTaxForm() {
        TaxForm taxForm = new TaxForm();
        taxForm.setId(this.id);
        taxForm.setUser(this.user.getUser());
        taxForm.setYear(this.year);
        taxForm.setTotalWages(this.totalWages);
        taxForm.setTotalFederalTaxesWithheld(this.totalFederalTaxesWithheld);
        taxForm.setTotalSocialSecurityTaxesWithheld(this.totalSocialSecurityTaxesWithheld);
        taxForm.setTotalMedicareTaxesWithheld(this.totalMedicareTaxesWithheld);
        taxForm.setCredits(this.credits);
        taxForm.setDeductions(this.deductions);
        taxForm.setRefund(this.refund);

        return taxForm;
    }
}

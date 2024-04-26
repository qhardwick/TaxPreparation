package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.TaxForm;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TaxFormDto {

    private int id;

    private UserDto user;

    private int year;

    private BigDecimal totalWages;

    private BigDecimal totalFederalTaxesWithheld;

    private BigDecimal totalSocialSecurityTaxesWithheld;

    private BigDecimal totalMedicareTaxesWithheld;

    private BigDecimal credits;

    private BigDecimal deductions;

    private BigDecimal refund;

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

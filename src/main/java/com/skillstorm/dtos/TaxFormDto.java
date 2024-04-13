package com.skillstorm.dtos;

import com.skillstorm.entities.TaxForm;
import com.skillstorm.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaxFormDto {

    private int id;

    private User user;

    private int year;

    private double totalWages;

    private double totalFederalTaxesWithheld;

    private double totalSocialSecurityTaxesWithheld;

    private double totalMedicareTaxesWithheld;

    private double refund;

    public TaxFormDto(TaxForm taxForm) {
        this.id = taxForm.getId();
        this.user = taxForm.getUser();
        this.year = taxForm.getYear();
        this.totalWages = taxForm.getTotalWages();
        this.totalFederalTaxesWithheld = taxForm.getTotalFederalTaxesWithheld();
        this.totalSocialSecurityTaxesWithheld = taxForm.getTotalSocialSecurityTaxesWithheld();
        this.totalMedicareTaxesWithheld = taxForm.getTotalMedicareTaxesWithheld();
        this.refund = taxForm.getRefund();
    }

    public TaxForm getTaxForm() {
        TaxForm taxForm = new TaxForm();
        taxForm.setId(this.id);
        taxForm.setUser(this.user);
        taxForm.setYear(this.year);
        taxForm.setTotalWages(this.totalWages);
        taxForm.setTotalFederalTaxesWithheld(this.totalFederalTaxesWithheld);
        taxForm.setTotalSocialSecurityTaxesWithheld(this.totalSocialSecurityTaxesWithheld);
        taxForm.setTotalMedicareTaxesWithheld(this.totalMedicareTaxesWithheld);
        taxForm.setRefund(this.refund);

        return taxForm;
    }
}

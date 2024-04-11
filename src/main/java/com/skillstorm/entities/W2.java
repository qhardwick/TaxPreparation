package com.skillstorm.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "w2s")
public class W2 {

    private String id;

    private String employer;

    private int year;

    private double wages;

    private double federalTaxesWithheld;

    private double socialSecurityTaxesWithheld;

    private double medicareTaxesWithheld;
}

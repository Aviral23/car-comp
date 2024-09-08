package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Specification implements Serializable {
    private int numberOfSeats;
    private int warrantyYears;
    private String engineHP;
    private EngineVariant engineVariant;
    private Boolean hasADAS;
    private Boolean hasABS;
    private Integer numberOfAirbags;

    public Specification() {
    }

    public Specification(int numberOfSeats, int warrantyYears, String engineHP, EngineVariant variant, Boolean hasADAS, Boolean hasABS, Integer numberOfAirbags) {
        this.numberOfSeats = numberOfSeats;
        this.warrantyYears = warrantyYears;
        this.engineHP = engineHP;
        this.engineVariant = variant;
        this.hasADAS = hasADAS;
        this.hasABS = hasABS;
        this.numberOfAirbags = numberOfAirbags;
    }
}



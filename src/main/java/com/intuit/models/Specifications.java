package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Specifications implements Serializable {
    private int numberOfSeats;
    private Safety safety;
    private int warrantyYears;
    private String engineHP;
    private String variant;

    public Specifications() {
    }

    public Specifications(int numberOfSeats, Safety safety, int warrantyYears, String engineHP, String variant) {
        this.numberOfSeats = numberOfSeats;
        this.safety = safety;
        this.warrantyYears = warrantyYears;
        this.engineHP = engineHP;
        this.variant = variant;
    }
}



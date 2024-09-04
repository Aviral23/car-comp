package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Warranty implements Serializable {
    private int years;
    private String coverageDetails;

}

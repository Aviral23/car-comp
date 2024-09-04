package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Specifications implements Serializable {
    private Seats seats;
    private Safety safety;
    private Warranty warranty;
}



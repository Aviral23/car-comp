package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Dimension implements Serializable {
    private double length;
    private double width;
    private double height;

    public Dimension(double length, double width, double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }
}

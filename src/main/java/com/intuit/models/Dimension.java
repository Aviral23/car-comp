package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Dimension implements Serializable {
    private double length;
    private double width;
    private double height;


}

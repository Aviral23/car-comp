package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Transmission implements Serializable {
    private String type;
    private int gears;

}


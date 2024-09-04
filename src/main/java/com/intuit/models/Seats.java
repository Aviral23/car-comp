package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Seats implements Serializable {
    private int numberOfSeats;
    private String material;
}

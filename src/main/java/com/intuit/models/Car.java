package com.intuit.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "catalog")
@Data
public class Car implements Serializable {
    @Id
    private String id;
    private String make;
    private String model;
    private int year;
    private String color;
    private double fuelLevel;
    private boolean isEngineOn;
    private String variant;
    private Feature features;
    private Specifications specifications;
    private Colors colors;
    private double price;
    private String type;
}

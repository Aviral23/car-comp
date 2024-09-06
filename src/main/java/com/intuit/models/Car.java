package com.intuit.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Data
public class Car implements Serializable {
    @Id
    private String id;
    private String make;
    private String model;
    private String name;
    private int manufactureYear;
    private String color;
    private Feature features;
    private Specifications specifications;
    private List<Image> images;
    private double price;
    private String type;

    public Car(String id, String make, String model, int manufactureYear, String color, Feature features, Specifications specifications, List<Image> images, double price, String type) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.manufactureYear = manufactureYear;
        this.color = color;
        this.features = features;
        this.specifications = specifications;
        this.images = images;
        this.price = price;
        this.type = type;
        this.name = this.getMake()+ " "+ this.getModel();
    }

    public void setName(String name) {
        this.name = this.getMake()+this.getModel();
    }

    public Car() {

    }
}

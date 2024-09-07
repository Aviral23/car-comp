package com.intuit.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String make;
    private String model;
    private String name;
    private int manufactureYear;
    private String color;
    private Feature features;
    private Specifications specifications;
    private List<Image> images;
    private BigDecimal price;
    private String type;

    public Car(String make, String model, int manufactureYear, String color, Feature features, Specifications specifications, List<Image> images, BigDecimal price, String type) {
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

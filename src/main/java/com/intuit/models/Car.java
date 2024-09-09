package com.intuit.models;

import javax.persistence.*;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String make;
    private String model;
    private String name;
    private int manufactureYear;
    private String color;
    @Transient
    private Feature feature;
    @Transient
    private Specification specification;
    @OneToMany(targetEntity=Image.class)
    private List<Image> images;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private String type;
    private String carVariant;
    @Column(nullable = false)
    private Integer featureId;
    @Column(nullable = false)
    private Integer specificationId;

    public void setName(String name) {
        this.name = this.getMake()+this.getModel();
    }

    public Car() {

    }

    public Car(String make, String model, String name, int manufactureYear, String color, Feature feature, Specification specification, List<Image> images, BigDecimal price, String type, String carVariant) {
        this.make = make;
        this.model = model;
        this.name = name;
        this.manufactureYear = manufactureYear;
        this.color = color;
        this.feature = feature;
        this.specification = specification;
        this.images = images;
        this.price = price;
        this.type = type;
        this.carVariant = carVariant;
    }

}

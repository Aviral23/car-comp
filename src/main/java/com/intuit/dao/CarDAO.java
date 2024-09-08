package com.intuit.dao;

import com.intuit.models.Image;

import lombok.Data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

public class CarDAO {
    private Long id;
    private String make;
    private String model;
    private String name;
    private int manufactureYear;
    private String color;
    private String feature;
    private String specification;
    private BigDecimal price;
    private String type;
    private String carVariant;
}

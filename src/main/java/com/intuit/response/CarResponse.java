package com.intuit.response;

import com.intuit.models.Car;
import com.intuit.models.Colors;
import com.intuit.models.Feature;
import com.intuit.models.Specifications;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CarResponse implements Serializable {
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

    public static CarResponse fromCar(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .make(car.getMake())
                .model(car.getModel())
                .year(car.getYear())
                .color(car.getColor())
                .fuelLevel(car.getFuelLevel())
                .isEngineOn(car.isEngineOn())
                .variant(car.getVariant())
                .features(car.getFeatures())
                .specifications(car.getSpecifications())
                .colors(car.getColors())
                .price(car.getPrice())
                .type(car.getType())
                .build();
    }

}

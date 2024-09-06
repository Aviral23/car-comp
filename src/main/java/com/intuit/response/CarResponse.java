package com.intuit.response;

import com.intuit.models.Car;
import com.intuit.models.Feature;
import com.intuit.models.Specifications;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

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
    private double price;
    private String type;
    private List<CarResponse> similarCars;

    public static CarResponse fromCar(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .make(car.getMake())
                .model(car.getModel())
                .year(car.getManufactureYear())
                .color(car.getColor())
                .features(car.getFeatures())
                .specifications(car.getSpecifications())
                .price(car.getPrice())
                .type(car.getType())
                .build();
    }
}

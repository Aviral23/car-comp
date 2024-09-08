package com.intuit.response;

import com.intuit.models.Car;
import com.intuit.models.Feature;
import com.intuit.models.Specification;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CarResponse implements Serializable {
    private Long id;
    private String make;
    private String model;
    private String name;
    private int year;
    private String color;
    private String variant;
    private Feature features;
    private Specification specifications;
    private BigDecimal price;
    private String type;
    private List<CarResponse> similarCars;

    public static CarResponse fromCar(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .make(car.getMake())
                .model(car.getModel())
                .year(car.getManufactureYear())
                .color(car.getColor())
                .features(car.getFeature())
                .specifications(car.getSpecification())
                .price(car.getPrice())
                .type(car.getType())
                .build();
    }
}

package com.intuit.service;

import com.intuit.response.CarResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CarService {
    List<CarResponse> getCarsByTypeAndPrice(String type, BigDecimal price);
    CarResponse getCarById(UUID id);
    CarResponse getCarByName(String name);
}


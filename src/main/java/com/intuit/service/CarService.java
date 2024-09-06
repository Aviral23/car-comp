package com.intuit.service;

import com.intuit.response.CarResponse;

import java.util.List;

public interface CarService {
    List<CarResponse> getCarsByTypeAndPrice(String type, Double price);
    CarResponse getCarById(String id);
}


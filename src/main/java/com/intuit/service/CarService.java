package com.intuit.service;

import com.intuit.models.Car;
import com.intuit.response.CarResponse;

import java.util.List;

public interface CarService {
    List<CarResponse> getCarsByTypeAndPrice(String type, double price);
}


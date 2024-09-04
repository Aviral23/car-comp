package com.intuit.service;

import com.intuit.exception.ValidationException;
import com.intuit.models.Car;
import com.intuit.response.CarResponse;
import com.intuit.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.intuit.repository.CarRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    private final RequestValidator requestValidator;

    @Autowired
    public CarServiceImpl(CarRepository carRepository,RequestValidator requestValidator) {
        this.carRepository = carRepository;
        this.requestValidator = requestValidator;
    }

    public List<CarResponse> getCarsByTypeAndPrice(String type, double price) {
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        Pageable pageable = PageRequest.of(0, 11, sort);

        try {
            List<Car> cars = carRepository.findTop10ByTypeAndPriceLessThanEqualOrderByPriceDesc(type, price, pageable);
            requestValidator.validateIfCarsExist(cars);
            return cars.stream()
                    .map(CarResponse::fromCar)
                    .collect(Collectors.toList());
        }
        catch (ValidationException validationException){
            LOGGER.error("No cars found for type and price: {}", validationException.getMessage());
            throw validationException;
        }
        catch (Exception e) {
            LOGGER.error("Error while fetching cars by type and price: {}", e.getMessage());
            throw e;
        }
    }
}

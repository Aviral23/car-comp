package com.intuit.service;

import com.intuit.exception.ValidationException;
import com.intuit.models.Car;
import com.intuit.repository.CarRepository;
import com.intuit.response.CarResponse;
import com.intuit.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public List<CarResponse> getCarsByTypeAndPrice(String type, BigDecimal price) {
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
            LOGGER.error("No cars found for this search: {}", validationException.getMessage());
            throw validationException;
        }
        catch (Exception e) {
            LOGGER.error("Error while fetching cars by type and price: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public CarResponse getCarById(UUID id) {
        Optional<Car> optionalCar;
        Car car = null;
        List<CarResponse> similarCarResponse = null;
        try {
            optionalCar = carRepository.findById(id);
            if (optionalCar.isPresent()) {
                car = optionalCar.get();
                similarCarResponse = carRepository.getTop10SimilarCars(car.getId(), car.getMake(), car.getType(), car.getPrice()).stream()
                        .map(CarResponse::fromCar)
                        .collect(Collectors.toList());
            } else throw new ValidationException("Invalid");
//            requestValidator.validateIfCarsExist(car);//TODO
        } catch (ValidationException v) {
            LOGGER.error("No car found for the given id: {}", v.getMessage());
            throw v;
        }
        CarResponse carResponse = CarResponse.fromCar(car);
        carResponse.setSimilarCars(similarCarResponse);
        return carResponse;
    }

    @Override
    public CarResponse getCarByName(String name) {
        Optional<Car> optionalCar;
        Car car = null;
        List<CarResponse> similarCarResponse = null;
        try {
            optionalCar = carRepository.findByName(name);
            if (optionalCar.isPresent()) {
                car = optionalCar.get();
                similarCarResponse = carRepository.getTop10SimilarCars(car.getId(), car.getMake(), car.getType(), car.getPrice()).stream()
                        .map(CarResponse::fromCar)
                        .collect(Collectors.toList());
            } else throw new ValidationException("Invalid");
//            requestValidator.validateIfCarsExist(car);//TODO
        } catch (ValidationException v) {
            LOGGER.error("No car found for the given id: {}", v.getMessage());
            throw v;
        }
        CarResponse carResponse = CarResponse.fromCar(car);
        carResponse.setSimilarCars(similarCarResponse);
        return carResponse;
    }
}

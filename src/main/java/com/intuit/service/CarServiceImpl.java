package com.intuit.service;

import com.intuit.exception.ValidationException;
import com.intuit.models.*;
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
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.Collections;
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

    @Cacheable(value = "cars", key = "#type")
    public List<CarResponse> getCarsByTypeAndPrice(String type, BigDecimal price) {
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        Pageable pageable = PageRequest.of(0, 11, sort);

        try {
            List<Car> cars = carRepository.findTop10ByTypeAndPriceLessThanEqualOrderByPriceDesc(type, price, pageable);
            cars = cars.stream().map(c -> getFeatureAndSpecificationForCar(Optional.of(c))).collect(Collectors.toList());
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
    @Cacheable(value = "cars", key = "#id")
    public CarResponse getCarById(Long id) {
        Optional<Car> optionalCar;
        Car car = null;
        List<CarResponse> similarCarResponse = null;
        try {
            optionalCar = carRepository.findById(id);
            if (optionalCar.isPresent()) {
                car = getFeatureAndSpecificationForCar(optionalCar);
                similarCarResponse = carRepository.getTop10SimilarCars(car.getId(), car.getMake(), car.getType(), car.getPrice()).stream()
                        .map(CarResponse::fromCar)
                        .collect(Collectors.toList());
                similarCarResponse = getFeatureAndSpecificationForSimilarCars(similarCarResponse);
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
    @Cacheable(value = "cars", key = "#name")
    public CarResponse getCarByName(String name) {
        Optional<Car> optionalCar;
        Car car = null;
        List<CarResponse> similarCarResponse = null;
        try {
            optionalCar = carRepository.findByName(name);
            if (optionalCar.isPresent()) {
                car = getFeatureAndSpecificationForCar(optionalCar);
                similarCarResponse = carRepository.getTop10SimilarCars(car.getId(), car.getMake(), car.getType(), car.getPrice()).stream()
                        .map(CarResponse::fromCar)
                        .collect(Collectors.toList());
                similarCarResponse = getFeatureAndSpecificationForSimilarCars(similarCarResponse);
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

    @Cacheable(value = "cars", key = "#featurespecscar")
    public Car getFeatureAndSpecificationForCar(Optional<Car> optionalCar) {
        Car car = optionalCar.get();
        List<Object[]> genericObject = null;
        List<Feature> features = null;
        List<Specification> specs = null;
        genericObject = carRepository.findFeature(Collections.singletonList(car.getId()));
        features = genericObject.stream()
                .map(result -> new Feature(
                        (Integer) result[0],
                        (Boolean) result[1],
                        (Boolean) result[2],
                        (Boolean) result[3],
                        GearTransmission.valueOf((String) result[4])))
                .collect(Collectors.toList());
        car.setFeature(features.get(0));
        genericObject = carRepository.findSpecs(Collections.singletonList(car.getId()));
        specs = genericObject.stream()
                .map(result -> new Specification(
                        (Integer) result[5],
                        (Integer) result[6],
                        (String) result[1],
                        EngineVariant.valueOf((String) result[7]),
                        (Boolean) result[3],
                        (Boolean) result[2],
                        (Integer) result[4]))
                .collect(Collectors.toList());
        car.setSpecification(specs.get(0));
        return car;
    }

    @Cacheable(value = "cars", key = "#featurespecscars")
    public List<CarResponse> getFeatureAndSpecificationForSimilarCars(List<CarResponse> similarCars) {
        List<Object[]> genericObject = null;
        List<Feature> features = null;
        List<Specification> specs = null;
        List<Long> similarCarIds = similarCars.stream().map(CarResponse::getId).collect(Collectors.toList());
        genericObject = carRepository.findFeature(similarCarIds);
        features = genericObject.stream()
                .map(result -> new Feature(
                        (Integer) result[0],
                        (Boolean) result[1],
                        (Boolean) result[2],
                        (Boolean) result[3],
                        GearTransmission.valueOf((String) result[4])))
                .collect(Collectors.toList());
        genericObject = carRepository.findSpecs(similarCarIds);
        specs = genericObject.stream()
                .map(result -> new Specification(
                        (Integer) result[5],
                        (Integer) result[6],
                        (String) result[1],
                        EngineVariant.valueOf((String) result[7]),
                        (Boolean) result[3],
                        (Boolean) result[2],
                        (Integer) result[4]))
                .collect(Collectors.toList());
        for(int i=0; i< similarCars.size(); i++) {
            similarCars.get(i).setFeatures(features.get(i));
            similarCars.get(i).setSpecifications(specs.get(i));
        }
        return similarCars;
    }
}

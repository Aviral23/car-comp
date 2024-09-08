package com.intuit.service;

import com.intuit.exception.ValidationException;
import com.intuit.models.Car;
import com.intuit.models.Feature;
import com.intuit.models.Specification;
import com.intuit.repository.CarRepository;
import com.intuit.request.CompareRequest;
import com.intuit.response.CarResponse;
import com.intuit.response.ComparisonList;
import com.intuit.response.ComparisonResponse;
import com.intuit.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComparisonLogicImpl implements ComparisonLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisonLogicImpl.class);
    private final CarService carService;
    private final FeatureComparator featureComparator;
    private final SpecificationsComparator specificationsComparator;
    private final RequestValidator requestValidator;
    private final RedisService redisService;

    @Autowired
    public ComparisonLogicImpl(CarService carService, FeatureComparator featureComparator,
                               SpecificationsComparator specificationsComparator, RequestValidator requestValidator, RedisService redisService) {
        this.carService = carService;
        this.featureComparator = featureComparator;
        this.specificationsComparator = specificationsComparator;
        this.requestValidator = requestValidator;
        this.redisService = redisService;
    }

    @Override
    public ComparisonList compareCars(CompareRequest compareRequest) {
        requestValidator.validateCompareRequest(compareRequest);
        try {
            Car firstCar = getCarById(compareRequest.getPresentCarId());
            Feature firstCarFeatures = firstCar.getFeature();
            Specification firstCarSpecification = firstCar.getSpecification();

            List<Car> listOfCars = getListOfCars(compareRequest.getIdList().stream().distinct().collect(Collectors.toList()));

            List<Feature> features = listOfCars.stream().map(Car::getFeature).collect(Collectors.toList());
            List<Specification> specifications = listOfCars.stream().map(Car::getSpecification).collect(Collectors.toList());

            List<ComparisonResponse> comparisonResponses = new ArrayList<>();

            ComparisonResponse featureComparison = featureComparator.compareFeatures(firstCarFeatures, features);
            ComparisonResponse specificationsComparison =
                    specificationsComparator.compareSpecifications(firstCarSpecification, specifications);

            comparisonResponses.add(featureComparison);
            comparisonResponses.add(specificationsComparison);
            ComparisonList comparisonList = new ComparisonList();
            comparisonList.setComparisonResponses(comparisonResponses);
            return comparisonList;
        } catch (ValidationException e) {
            LOGGER.error("Error during comparison: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        } catch (Exception exception) {
            LOGGER.error("Unexpected error occurred: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    private Car getCarById(Long id) {
        Car car = null;
        try {
            LOGGER.info("Fetched from cache");
            car = redisService.getCachedCar(id);
        }catch (Exception ex){
            LOGGER.error("Exception while fetching from redis");
        }
        if(car == null) {
            LOGGER.info("Fetching from database");
            CarResponse carResponse = carService.getCarById(id);
            car = new Car(carResponse.getMake(), carResponse.getModel(), carResponse.getName(), carResponse.getYear(), carResponse.getColor(), carResponse.getFeatures(), carResponse.getSpecifications(), null, carResponse.getPrice(), carResponse.getType(), carResponse.getVariant());
            try {
                redisService.putCarToCache(id, car);
            } catch (Exception exception) {
                LOGGER.error("Exception in saving car in cache");
            }
        }
        return car;
    }

    private List<Car> getListOfCars(List<Long> carIds) {
        List<Car> list = new ArrayList<>();
        for (Long id : carIds) {
            list.add(getCarById(id));
        }
        return list;
    }
}

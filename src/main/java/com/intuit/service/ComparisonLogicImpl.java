package com.intuit.service;

import com.intuit.exception.ValidationException;
import com.intuit.models.Car;
import com.intuit.models.Feature;
import com.intuit.models.Specifications;
import com.intuit.repository.CarRepository;
import com.intuit.request.CompareRequest;
import com.intuit.response.ComparisonList;
import com.intuit.response.ComparisonResponse;
import com.intuit.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ComparisonLogicImpl implements ComparisonLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisonLogicImpl.class);

    private final CarRepository carRepository;
    private final FeatureComparator featureComparator;
    private final SpecificationsComparator specificationsComparator;
    private final RequestValidator requestValidator;

    private final RedisService redisService;

    @Autowired
    public ComparisonLogicImpl(CarRepository carRepository, FeatureComparator featureComparator,
                               SpecificationsComparator specificationsComparator, RequestValidator requestValidator, RedisService redisService) {
        this.carRepository = carRepository;
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
            Feature firstCarFeatures = firstCar.getFeatures();
            Specifications firstCarSpecifications = firstCar.getSpecifications();

            List<Car> listOfCars = getListOfCars(compareRequest.getIdList().stream().distinct().collect(Collectors.toList()));

            List<Feature> features = listOfCars.stream().map(Car::getFeatures).collect(Collectors.toList());
            List<Specifications> specifications = listOfCars.stream().map(Car::getSpecifications).collect(Collectors.toList());

            List<ComparisonResponse> comparisonResponses = new ArrayList<>();

            ComparisonResponse featureComparison = featureComparator.compareFeatures(firstCarFeatures, features);
            ComparisonResponse specificationsComparison =
                    specificationsComparator.compareSpecifications(firstCarSpecifications, specifications);

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

    private Car getCarById(UUID id) {
        Car car = null;
        try {
            redisService.getCachedCar(id);
        }catch (Exception ex){
            LOGGER.error("Exception while fetching from redis");
        }
        LOGGER.info("Fetching from database");
        Optional<Car> optionalCar = carRepository.findById(id);
        if(optionalCar == null || !optionalCar.isPresent())
            throw new ValidationException("Car not found with ID: " + id);
        car = optionalCar.get();
        try {
            redisService.putCarToCache(id, car);
        }
        catch (Exception exception){
            LOGGER.error("Exception in saving car in cache");
        }

        return car;
    }


    private List<Car> getListOfCars(List<UUID> carIds) {
        List<Car> list = new ArrayList<>();
        for (UUID id : carIds) {
            list.add(getCarById(id));
        }
        return list;
    }
}

package com.intuit.service;

import com.intuit.exception.ValidationException;
import com.intuit.models.*;
import com.intuit.repository.CarRepository;
import com.intuit.request.CompareRequest;
import com.intuit.response.ComparisonResponse;
import com.intuit.response.ComparisonList;
import com.intuit.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.intuit.utils.Constants.CAR_KEY_PREFIX;

@Service
public class ComparisonLogicImpl implements ComparisonLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisonLogicImpl.class);

    private final CarRepository carRepository;
    private final FeatureComparator featureComparator;
    private final SpecificationsComparator specificationsComparator;
    private final RequestValidator requestValidator;

    private final RedisService redisService;

    private final ExecutorService executorService;

    @Autowired
    public ComparisonLogicImpl(CarRepository carRepository, FeatureComparator featureComparator,
                               SpecificationsComparator specificationsComparator, RequestValidator requestValidator, RedisService redisService,@Value("${comparator.thread.pool:1}") int threadPoolSize) {
        this.carRepository = carRepository;
        this.featureComparator = featureComparator;
        this.specificationsComparator = specificationsComparator;
        this.requestValidator = requestValidator;
        this.redisService = redisService;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);

    }

    @Override
    public ComparisonList compare(CompareRequest compareRequest) {
        requestValidator.validateCompareRequest(compareRequest);
        try {
            Car firstCar = getCarById(compareRequest.getViewingCarId());
            Feature firstCarFeatures = firstCar.getFeatures();
            Specifications firstCarSpecifications = firstCar.getSpecifications();

            List<Car> listOfCars = getListOfCars(compareRequest.getIdList().stream().distinct().collect(Collectors.toList()));

            List<Feature> features = listOfCars.stream().map(Car::getFeatures).collect(Collectors.toList());
            List<Specifications> specifications = listOfCars.stream().map(Car::getSpecifications).collect(Collectors.toList());

            List<ComparisonResponse> comparisonResponses = new ArrayList<>();


            CompletableFuture<ComparisonResponse> featureComparisonFuture = CompletableFuture.supplyAsync(() ->
                    featureComparator.compareFeatures(firstCarFeatures, features), executorService
            );
            CompletableFuture<ComparisonResponse> specificationsComparisonFuture = CompletableFuture.supplyAsync(() ->
                    specificationsComparator.compareSpecifications(firstCarSpecifications, specifications), executorService
            );

            comparisonResponses.add(featureComparisonFuture.get());
            comparisonResponses.add(specificationsComparisonFuture.get());
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

    private Car getCarById(String id) {
        Car car = null;
        try {
            redisService.getCachedCar(id);
        }catch (Exception ex){
            LOGGER.error("Exception while fetching from redis");
        }
        if(car == null){
            LOGGER.info("Fetching from database");
            Optional<Car> optionalCar = carRepository.findById(id);
            car = optionalCar.orElseThrow(() -> new ValidationException("Car not found with ID: " + id));
        }
        try {
            redisService.putCarToCache(id, car);
        }
        catch (Exception exception){
            LOGGER.error("Exception in saving car in cache");
        }

        return car;
    }


    private List<Car> getListOfCars(List<String> carIds) {
        List<Car> list = new ArrayList<>();
        for (String id : carIds) {
            list.add(getCarById(id));
        }
        return list;
    }
}

package com.intuit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.request.CompareRequest;
import com.intuit.response.CarResponse;
import com.intuit.response.ComparisonList;
import com.intuit.service.CarService;
import com.intuit.service.ComparisonLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/car/v1")
public class CarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);
    private final CarService carService;
    private final ComparisonLogic comparisonLogic;

    public CarController(CarService carService, ComparisonLogic comparisonLogic) {
        this.carService = carService;
        this.comparisonLogic = comparisonLogic;
    }

    @ResponseBody
    @GetMapping(value = "/type-and-price", produces = "application/json")
    public ResponseEntity<List<CarResponse>> getCarsByTypeAndPrice(
            @RequestParam Map<String, Object> params,
            @RequestParam String type,
            @RequestParam Double price
    ) {
        List<CarResponse> cars = carService.getCarsByTypeAndPrice(params.get("type").toString(), (Double) params.get("price"));
        LOGGER.info("Retrieved cars by type '{}' and price '{}'", type, price);
        return new ResponseEntity<>(cars, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "/id", produces = "application/json")
    public ResponseEntity<CarResponse> getCarById(
            @RequestParam String id
    ) {
        CarResponse car = carService.getCarById(id);
        LOGGER.info("Retrieved car by id '{}'", id);
        return new ResponseEntity<>(car, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "/compare/car", produces = "application/json")
    public ResponseEntity<ComparisonList> selectCarsForComparison(
            @RequestBody CompareRequest compareRequest
    ) throws JsonProcessingException {
        ComparisonList comparisonResponses = comparisonLogic.compare(compareRequest);
        LOGGER.info("Performed comparison for car IDs: {}", compareRequest);
        return new ResponseEntity<>(comparisonResponses, HttpStatus.OK);
    }
}

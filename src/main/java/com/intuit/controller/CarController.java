package com.intuit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.request.CompareRequest;
import com.intuit.response.CarResponse;
import com.intuit.response.ComparisonList;
import com.intuit.service.CarService;
import com.intuit.service.ComparisonLogic;
import datadog.trace.api.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
//import datadog.trace.api.DDTags;
//import datadog.trace.api.Trace;

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
    @Trace(operationName = "findbytypeprice.request")
    public ResponseEntity<List<CarResponse>> getCarsByTypeAndPrice(
            @RequestParam Map<String, String> params
    ) {
        LOGGER.info("Got params: cars by type '{}' and price '{}'", params.get("type"), params.get("price"));
        List<CarResponse> cars = carService.getCarsByTypeAndPrice(params.get("type"), new BigDecimal(params.get("price")));
        LOGGER.info("Retrieved cars by type '{}' and price '{}'", params.get("type"), params.get("price"));
        return new ResponseEntity<>(cars, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "/id", produces = "application/json")
    @Trace(operationName = "findbyid.request")
    public ResponseEntity<CarResponse> getCarById(
            @RequestParam Long id
    ) {
        LOGGER.info("Request to get car by id '{}'", id);
        CarResponse car = carService.getCarById(id);
        LOGGER.info("Retrieved car by id '{}'", id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/name", produces = "application/json")
    @Trace(operationName = "findbyname.request")
    public ResponseEntity<CarResponse> getCarByName(
            @RequestParam String name
    ) {
        CarResponse car = carService.getCarByName(name);
        LOGGER.info("Retrieved car by name '{}'", name);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/compare", produces = "application/json")
    @Trace(operationName = "compare.request")
    public ResponseEntity<ComparisonList> selectCarsForComparison(
            @RequestBody CompareRequest compareRequest
    ) throws JsonProcessingException {
        ComparisonList comparisonResponses = comparisonLogic.compareCars(compareRequest);
        LOGGER.info("Performed comparison for car IDs: {}", compareRequest);
        return new ResponseEntity<>(comparisonResponses, HttpStatus.OK);
    }
}

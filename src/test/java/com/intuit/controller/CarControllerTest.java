package com.intuit.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.exception.ValidationException;
import com.intuit.request.CompareRequest;
import com.intuit.response.CarResponse;
import com.intuit.response.ComparisonList;
import com.intuit.service.CarService;
import com.intuit.service.ComparisonLogic;
import com.intuit.service.RedisService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CarControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private ComparisonLogic comparisonLogic;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private CarController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetCarsByTypeAndPrice() {
        List<CarResponse> mockedCars = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("type", "SUV");
        params.put("price", "25000");
        when(carService.getCarsByTypeAndPrice(anyString(), any(BigDecimal.class))).thenReturn(mockedCars);
        ResponseEntity<List<CarResponse>> response = controller.getCarsByTypeAndPrice(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockedCars, response.getBody());
        Mockito.verify(carService, Mockito.times(1)).getCarsByTypeAndPrice(Mockito.anyString(), Mockito.any(BigDecimal.class));
    }

    @Test
    public void testGetCarsByTypeAndPriceWhenNoTypeSpecified() {
        List<CarResponse> mockedCars = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("price", "25000");
        ResponseEntity<List<CarResponse>> response = controller.getCarsByTypeAndPrice(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockedCars, response.getBody());
        Mockito.verify(carService, Mockito.times(1)).getCarsByTypeAndPrice(any(), Mockito.any(BigDecimal.class));
    }

    @Test
    public void testGetCarsByTypeAndPriceWhenDataDoesNotExist() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "");
        params.put("price", "0.0");
        when(carService.getCarsByTypeAndPrice(anyString(), any(BigDecimal.class))).thenReturn(null);
        ResponseEntity<List<CarResponse>> response = controller.getCarsByTypeAndPrice(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        Mockito.verify(carService, Mockito.times(1)).getCarsByTypeAndPrice(Mockito.anyString(), Mockito.any(BigDecimal.class));
        Mockito.verifyNoMoreInteractions(carService);

    }

    @Test
    public void testSelectCarsForComparison() throws JsonProcessingException {
        ComparisonList mockedComparisonList = new ComparisonList();
        when(comparisonLogic.compareCars(any(CompareRequest.class))).thenReturn(mockedComparisonList);
        ResponseEntity<ComparisonList> response = controller.selectCarsForComparison(new CompareRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockedComparisonList, response.getBody());
        Mockito.verify(comparisonLogic, Mockito.times(1)).compareCars(Mockito.any());
        Mockito.verifyNoMoreInteractions(comparisonLogic);

    }

    @Test(expected = ValidationException.class)
    public void testSelectCarsForComparisonWhenCorrectRequestIsNotSend() throws JsonProcessingException {
        when(comparisonLogic.compareCars(any(CompareRequest.class))).thenThrow(ValidationException.class);
        controller.selectCarsForComparison(new CompareRequest());
    }

}

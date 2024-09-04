package com.intuit.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.exception.ValidationException;
import com.intuit.request.CompareRequest;
import com.intuit.response.CarResponse;
import com.intuit.response.ComparisonList;
import com.intuit.service.ComparisonLogic;
import com.intuit.service.CarService;
import com.intuit.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private ComparisonLogic comparisonLogic;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private Controller controller;


    @Test
    public void testGetCarsByTypeAndPrice() {
        List<CarResponse> mockedCars = new ArrayList<>();
        when(carService.getCarsByTypeAndPrice(anyString(), anyDouble())).thenReturn(mockedCars);
        ResponseEntity<List<CarResponse>> response = controller.getCarsByTypeAndPrice("SUV", 25000.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockedCars, response.getBody());
        Mockito.verify(carService, Mockito.times(1)).getCarsByTypeAndPrice(Mockito.anyString(), Mockito.anyDouble());
        Mockito.verifyZeroInteractions(carService);

    }

    @Test
    public void testGetCarsByTypeAndPriceWhenDataDoesNotExist() {
        when(carService.getCarsByTypeAndPrice(anyString(), anyDouble())).thenReturn(null);
        ResponseEntity<List<CarResponse>> response = controller.getCarsByTypeAndPrice("", 0.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        Mockito.verify(carService, Mockito.times(1)).getCarsByTypeAndPrice(Mockito.anyString(), Mockito.anyDouble());
        Mockito.verifyNoMoreInteractions(carService);

    }

    @Test
    public void testSelectCarsForComparison() throws JsonProcessingException {
        ComparisonList mockedComparisonList = new ComparisonList();
        when(comparisonLogic.compare(any(CompareRequest.class))).thenReturn(mockedComparisonList);
        ResponseEntity<ComparisonList> response = controller.selectCarsForComparison(new CompareRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockedComparisonList, response.getBody());
        Mockito.verify(comparisonLogic, Mockito.times(1)).compare(Mockito.any());
        Mockito.verifyNoMoreInteractions(comparisonLogic);

    }

    @Test(expected = ValidationException.class)
    public void testSelectCarsForComparisonWhenCorrectRequestIsNotSend() throws JsonProcessingException {
        when(comparisonLogic.compare(any(CompareRequest.class))).thenThrow(ValidationException.class);
        controller.selectCarsForComparison(new CompareRequest());
    }

}

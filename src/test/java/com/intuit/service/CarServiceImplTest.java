package com.intuit.service;

import com.intuit.models.*;
import com.intuit.repository.CarRepository;
import com.intuit.response.CarResponse;
import com.intuit.validator.RequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CarServiceImplTest {
    private List<Car> carList;
    private Car car1, car2, car3, car4;


    private Feature feature;
    private Specification specification;

    @Mock
    private CarRepository carRepository;

    @Mock
    private RequestValidator requestValidator;

    @InjectMocks
    private CarServiceImpl carService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        car1 = new Car();
        car1.setId(10L);
        car1.setName("Maruti Baleno");
        car1.setPrice(new BigDecimal(45000));
        car1.setType("Hatchback");
        car2 = new Car();
        car2.setId(21L);
        car2.setName("Toyota Camry");
        car2.setPrice(new BigDecimal(50000));
        car2.setType("Sedan");
        car3 = new Car();
        car3.setId(20L);
        car3.setName("Honda Civic");
        car3.setPrice(new BigDecimal(70000));
        car3.setType("Coupe");
        car4 = new Car();
        car4.setId(25L);
        car4.setName("Ford Mustang");
        car4.setPrice(new BigDecimal(80000));
        car4.setType("Sedan");
        carList = Arrays.asList(car1, car2, car3, car4);
        feature = new Feature(10, true, false, true, GearTransmission.MANUAL);
        specification = new Specification(6, 6, "180hp", EngineVariant.EV, true, false, 5);
    }

    @Test
    public void testGetFeatureAndSpecificationForCar() {
        Object[] featureData = {1, true, false, true, "AUTOMATIC"};
        Object[] specData = {1, "180 hp", true, false, 3, 4, 4, "EV"};

        when(carRepository.findFeature(Collections.singletonList(car1.getId()))).thenReturn(Collections.singletonList(featureData));
        when(carRepository.findSpecs(Collections.singletonList(car1.getId()))).thenReturn(Collections.singletonList(specData));

        Optional<Car> optionalCar = Optional.of(car1);

        Feature expectedFeature = new Feature(1, true, false, true, GearTransmission.AUTOMATIC);
        car2.setFeature(expectedFeature);
        Specification expectedSpec = new Specification(4, 4, "180 hp", EngineVariant.EV, true, true, 4);
        car2.setSpecification(expectedSpec);

        Car resultCar = carService.getFeatureAndSpecificationForCar(optionalCar);
        assertEquals(expectedFeature.getId(), resultCar.getFeature().getId());
        assertEquals(expectedFeature.getHasNavigation(), resultCar.getFeature().getHasNavigation());
        assertEquals(expectedSpec.getEngineHP(), resultCar.getSpecification().getEngineHP());
        assertEquals(expectedSpec.getWarrantyYears(), resultCar.getSpecification().getWarrantyYears());
        assertEquals(expectedSpec.getEngineVariant(), resultCar.getSpecification().getEngineVariant());
        assertEquals(expectedSpec.getNumberOfSeats(), resultCar.getSpecification().getNumberOfSeats());
    }

    @Test
    public void testGetCarsByTypeAndPrice() {
        when(carRepository.findTop10ByTypeContainingAndPriceLessThanEqualOrderByPriceDesc(anyString(), any(BigDecimal.class), any(Pageable.class))).
                thenReturn(carList);
        Object[] featureData = {1, true, false, true, "AUTOMATIC"};
        Object[] specData = {1, "180 hp", true, false, 3, 4, 4, "EV"};

        when(carRepository.findFeature(anyList())).thenReturn(Collections.singletonList(featureData));
        when(carRepository.findSpecs(anyList())).thenReturn(Collections.singletonList(specData));

        List<CarResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(CarResponse.fromCar(car1));
        expectedResponse.add(CarResponse.fromCar(car2));
        expectedResponse.add(CarResponse.fromCar(car3));
        expectedResponse.add(CarResponse.fromCar(car4));

        List<CarResponse> result = carService.getCarsByTypeAndPrice("SUV", new BigDecimal(45000));

        assertEquals(expectedResponse.size(), result.size());
        verify(requestValidator, times(1)).validateIfCarsExist(carList);
    }
}

package com.intuit.service;

import com.intuit.models.Car;
import com.intuit.models.Feature;
import com.intuit.models.Specification;
import com.intuit.repository.CarRepository;
import com.intuit.request.CompareRequest;
import com.intuit.response.CarResponse;
import com.intuit.response.ComparisonList;
import com.intuit.validator.RequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ComparisonLogicImplTest {

    @Mock
    private CarService carService;
    @Mock
    private CarRepository carRepository;

    @Mock
    private ComparatorUtils comparatorUtils;

    @Mock
    private SpecificationsComparator specificationsComparator;

    @Mock
    private FeatureComparator featureComparator;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private RedisService redisService;

    @Mock
    private CarResponse carResponse;

    @InjectMocks
    private ComparisonLogicImpl carComparisonLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Car car1 = buildCar(10L,new Feature(),new Specification());
        carResponse = CarResponse.fromCar(car1);
        when(carService.getCarById(anyLong())).thenReturn(carResponse);
    }
    @Test
    public void testCompareCarsSuccess() throws Exception {


        Car car1 = buildCar(10L,new Feature(),new Specification());

        CompareRequest compareRequest = new CompareRequest();
        compareRequest.setPresentCarId(10L);
        compareRequest.setIdList(Arrays.asList(11L, 12L));

        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car1));

        ComparisonList comparisonList = carComparisonLogic.compareCars(compareRequest);

        assertNotNull(comparisonList);
        verify(requestValidator, times(1)).validateCompareRequest(compareRequest);
        verify(carService, times(3)).getCarById(anyLong());
        verify(specificationsComparator, times(1)).compareSpecifications(any(), any());

        verifyNoMoreInteractions(requestValidator);
        verifyNoMoreInteractions(carRepository);
        verifyNoMoreInteractions(specificationsComparator);
    }

    @Test
    public void testCompareCarsWhenCarNotFound() {
        CompareRequest compareRequest = new CompareRequest();
        Car car1 = buildCar(10L,new Feature(),new Specification());
        compareRequest.setPresentCarId(10L);
        compareRequest.setIdList(Arrays.asList(11L, 12L));
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car1));
        carComparisonLogic.compareCars(compareRequest);
        verify(requestValidator, times(1)).validateCompareRequest(compareRequest);
        verify(carService, times(3)).getCarById(anyLong());
        verifyNoInteractions(comparatorUtils);
    }

    private Car buildCar(Long id, Feature feature, Specification specification) {
        Car car = new Car();
        car.setSpecification(specification);
        car.setId(id);
        car.setFeature(feature);
        return car;
    }

}

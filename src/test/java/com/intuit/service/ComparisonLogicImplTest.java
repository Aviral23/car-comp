package com.intuit.service;

import com.intuit.exception.ValidationException;
import com.intuit.models.Car;
import com.intuit.models.Feature;
import com.intuit.models.Specifications;
import com.intuit.repository.CarRepository;
import com.intuit.request.CompareRequest;
import com.intuit.response.ComparisonList;
import com.intuit.validator.RequestValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ComparisonLogicImplTest {

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

    @InjectMocks
    private ComparisonLogicImpl carComparisonLogic;


    @Test
    public void testCompareCarsSuccess() throws Exception {


        Car car1 = buildCar("id1",new Feature(),new Specifications());
        Car car2 = buildCar("id2",new Feature(),new Specifications());
        Car car3 = buildCar("id3",new Feature(),new Specifications());

        CompareRequest compareRequest = new CompareRequest();
        compareRequest.setViewingCarId("id1");
        compareRequest.setIdList(Arrays.asList("id3","id2"));

        Mockito.when(carRepository.findById("id1")).thenReturn(car1);
        Mockito.when(carRepository.findById("id2")).thenReturn(car2);
        Mockito.when(carRepository.findById("id3")).thenReturn(car3);

        ComparisonList comparisonList = carComparisonLogic.compare(compareRequest);

        assertNotNull(comparisonList);
        verify(requestValidator, times(1)).validateCompareRequest(compareRequest);
        verify(carRepository, times(3)).findById(anyString());
        verify(specificationsComparator, times(1)).compareSpecifications(any(), any());

        verifyNoMoreInteractions(requestValidator);
        verifyNoMoreInteractions(carRepository);
        verifyNoMoreInteractions(specificationsComparator);
    }

    @Test
    public void testCompareCarsWhenCarNotFound() {
        CompareRequest compareRequest = new CompareRequest();
        compareRequest.setViewingCarId("id1");
        compareRequest.setIdList(Arrays.asList("id3","id2"));
        when(carRepository.findById(anyString())).thenReturn(any(Car.class));

        assertThrows(ValidationException.class, () -> carComparisonLogic.compare(compareRequest));
        verify(requestValidator, times(1)).validateCompareRequest(compareRequest);
        verify(carRepository, times(1)).findById(anyString());
        verifyNoInteractions(comparatorUtils);
        verifyNoInteractions(specificationsComparator);
    }

    @Test
    public void testCompareCarsWhenMoreThanTwoCarsNotFound() {
        CompareRequest compareRequest = new CompareRequest();
        compareRequest.setViewingCarId("id1");
        compareRequest.setIdList(Arrays.asList("id3","id2","id4"));

        assertThrows(ValidationException.class, () -> carComparisonLogic.compare(compareRequest));
        verify(requestValidator, times(1)).validateCompareRequest(compareRequest);
        verify(carRepository, times(1)).findById(anyString());
        verifyNoInteractions(comparatorUtils);
        verifyNoInteractions(specificationsComparator);
    }

    private Car buildCar(String id, Feature feature, Specifications specifications) {
        Car car = new Car();
        car.setSpecifications(specifications);
        car.setId(id);
        car.setFeatures(feature);
        return car;
    }

}

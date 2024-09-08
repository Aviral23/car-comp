package com.intuit.service;

import com.intuit.models.EngineVariant;
import com.intuit.models.Specification;
import com.intuit.response.ComparisonResponse;
import com.intuit.response.FeatureResponse;
import com.intuit.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;

@SpringBootTest
public class SpecificationsComparatorImplTest {
    private Specification spec1;
    private Specification spec2;
    private Specification currCar;
    private List<Specification> specsList;
    @InjectMocks
    private SpecificationsComparatorImpl specificationsComparator;
    @Mock
    private ComparisonResponse comparisonResponse;

    @Mock
    private FeatureResponse featureResponse;
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        currCar = new Specification();
        currCar.setNumberOfSeats(7);
        currCar.setWarrantyYears(5);
        currCar.setEngineHP("1502");
        currCar.setEngineVariant(EngineVariant.DIESEL);
        currCar.setHasADAS(true);
        currCar.setHasABS(true);
        currCar.setNumberOfAirbags(4);

        spec1 = new Specification();
        spec1.setNumberOfSeats(5);
        spec1.setWarrantyYears(3);
        spec1.setEngineHP("150");
        spec1.setEngineVariant(EngineVariant.DIESEL);
        spec1.setHasADAS(true);
        spec1.setHasABS(true);
        spec1.setNumberOfAirbags(4);

        spec2 = new Specification();
        spec2.setNumberOfSeats(7);
        spec2.setWarrantyYears(5);
        spec2.setEngineHP("180");
        spec2.setEngineVariant(EngineVariant.PETROL);
        spec2.setHasADAS(false);
        spec2.setHasABS(false);
        spec2.setNumberOfAirbags(4);

        specsList = new ArrayList<>();
        specsList.add(spec1);
        specsList.add(spec2);
    }
    @Test
    public void testCompareSpecificationsReturnsCorrectGroupName() {
        comparisonResponse = specificationsComparator.compareSpecifications(currCar, specsList);
        Assertions.assertNotNull(comparisonResponse);
        Assertions.assertEquals(Constants.SPECIFICATION, comparisonResponse.getGroupName());
    }

    @Test
    public void testCompareSpecificationsReturnsCorrectNumSeatsComparison() {
        comparisonResponse = specificationsComparator.compareSpecifications(currCar, specsList);
        Assertions.assertNotNull(comparisonResponse);
        List<FeatureResponse> featureResponses = comparisonResponse.getFeature();
        Assertions.assertEquals(5, featureResponses.size());
        featureResponse = featureResponses.get(4);
        Assertions.assertEquals(Constants.NUMBER_OF_SEATS, featureResponse.getName());
        Assertions.assertEquals(7, Integer.parseInt(featureResponse.getValues().get(0)));
        Assertions.assertEquals(5, Integer.parseInt(featureResponse.getValues().get(1)));
        Assertions.assertFalse(featureResponse.isSimilarity());
    }

}

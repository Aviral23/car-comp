package com.intuit.service;

import com.intuit.models.Feature;
import com.intuit.models.GearTransmission;
import com.intuit.response.ComparisonResponse;
import com.intuit.response.FeatureResponse;
import com.intuit.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FeatureComparatorImplTest {
        private Feature feat1;
        private Feature feat2;
        private Feature currCarFeat;
        private List<Feature> featList;
        @InjectMocks
        private FeatureComparatorImpl featureComparator;
        @Mock
        private ComparisonResponse comparisonResponse;

        @Mock
        private FeatureResponse featureResponse;
        @Before
        public void setUp() {
            MockitoAnnotations.openMocks(this);
            currCarFeat = new Feature();
            currCarFeat.setHasBluetooth(true);
            currCarFeat.setGearTransmission(GearTransmission.AUTOMATIC);
            currCarFeat.setHasNavigation(true);
            currCarFeat.setHasRearCamera(true);

            feat1 = new Feature();
            feat1.setHasBluetooth(false);
            feat1.setGearTransmission(GearTransmission.MANUAL);
            feat1.setHasNavigation(true);
            feat1.setHasRearCamera(true);

            feat2 = new Feature();
            feat2.setHasBluetooth(false);
            feat2.setGearTransmission(GearTransmission.AUTOMATIC);
            feat2.setHasNavigation(true);
            feat2.setHasRearCamera(false);

            featList = new ArrayList<>();
            featList.add(feat1);
            featList.add(feat2);
        }
        @Test
        public void testCompareSpecificationsReturnsCorrectGroupName() {
            comparisonResponse = featureComparator.compareFeatures(currCarFeat, featList);
            Assertions.assertNotNull(comparisonResponse);
            Assertions.assertEquals(Constants.FEATURES, comparisonResponse.getGroupName());
        }

        @Test
        public void testCompareFeaturesReturnsTrueForSimilarity() {
            comparisonResponse = featureComparator.compareFeatures(currCarFeat, featList);
            Assertions.assertNotNull(comparisonResponse);
            List<FeatureResponse> featureResponses = comparisonResponse.getFeature();
            Assertions.assertEquals(3, featureResponses.size());
            featureResponse = featureResponses.get(1);
            Assertions.assertEquals(Constants.HAS_NAVIGATION, featureResponse.getName());
            Assertions.assertTrue(Boolean.parseBoolean(featureResponse.getValues().get(0)));
            Assertions.assertTrue(Boolean.parseBoolean(featureResponse.getValues().get(1)));
            Assertions.assertTrue(featureResponse.isSimilarity());
        }

    @Test
    public void testCompareFeaturesReturnsFalseForSimilarity() {
        comparisonResponse = featureComparator.compareFeatures(currCarFeat, featList);
        Assertions.assertNotNull(comparisonResponse);
        List<FeatureResponse> featureResponses = comparisonResponse.getFeature();
        Assertions.assertEquals(3, featureResponses.size());
        featureResponse = featureResponses.get(2);
        Assertions.assertEquals(Constants.HAS_BLUETOOTH, featureResponse.getName());
        Assertions.assertTrue(Boolean.parseBoolean(featureResponse.getValues().get(0)));
        Assertions.assertFalse(Boolean.parseBoolean(featureResponse.getValues().get(1)));
        Assertions.assertFalse(featureResponse.isSimilarity());
    }
}

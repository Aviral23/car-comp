package com.intuit.service;

import com.intuit.models.Feature;
import com.intuit.response.ComparisonResponse;
import com.intuit.response.FeatureResponse;
import com.intuit.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.intuit.service.ComparatorUtils.getAllValuesForType;
import static com.intuit.service.ComparatorUtils.isCommonType;

@Service
public class FeatureComparatorImpl implements FeatureComparator {

    @Override
    public ComparisonResponse compareFeatures(Feature features, List<Feature> featuresOfOthers) {
        ComparisonResponse comparisonResponse = new ComparisonResponse();
        comparisonResponse.setGroupName(Constants.FEATURES);
        List<Boolean> bluetoothList = featuresOfOthers.stream()
                .map(Feature::getHasBluetooth)
                .collect(Collectors.toList());

        compareFeatures(Constants.HAS_BLUETOOTH, features.getHasBluetooth(), bluetoothList, comparisonResponse);

        List<Boolean> navigationList = featuresOfOthers.stream()
                .map(Feature::getHasNavigation)
                .collect(Collectors.toList());
        compareFeatures(Constants.HAS_NAVIGATION, features.getHasNavigation(), navigationList, comparisonResponse);

        List<Boolean> rearCameraList = featuresOfOthers.stream()
                .map(Feature::getHasRearCamera)
                .collect(Collectors.toList());
        compareFeatures(Constants.HAS_REAR_CAMERA, features.getHasRearCamera(), rearCameraList, comparisonResponse);
        return comparisonResponse;
    }

    private void compareFeatures(String featureName, Boolean hasFeature, List<Boolean> hasFeatureList, ComparisonResponse comparisonResponse) {
        List<FeatureResponse> featureResponses = new ArrayList<>();
        compareAndAddFeature(featureName, hasFeature, hasFeatureList, featureResponses);
        comparisonResponse.setFeature(featureResponses);
    }

    private void compareAndAddFeature(String featureName, Boolean value, List<Boolean> hasFeatureList, List<FeatureResponse> featureResponses) {

        boolean isCommonValue = isCommonType(value, hasFeatureList);

        featureResponses.add(FeatureResponse.builder()
                .name(featureName)
                .values(getAllValuesForType(value, hasFeatureList))
                .isCommonValue(isCommonValue)
                .build());
    }

}

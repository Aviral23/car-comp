package com.intuit.service;

import com.intuit.models.Feature;
import com.intuit.response.ComparisonResponse;
import com.intuit.response.FeatureResponse;
import com.intuit.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.intuit.service.ComparatorUtils.getAllValuesForType;
import static com.intuit.service.ComparatorUtils.isCommonType;

@Service
public class FeatureComparatorImpl implements FeatureComparator {

    private ComparisonResponse comparisonResponse;
    @Override
    public ComparisonResponse compareFeatures(Feature features, List<Feature> featuresOfOthers) {
        comparisonResponse = new ComparisonResponse();
        comparisonResponse.setGroupName(Constants.FEATURES);
        List<String> bluetoothList = featuresOfOthers.stream()
                .map(s -> String.valueOf(s.getHasBluetooth()))
                .collect(Collectors.toList());

        compareFeatures(Constants.HAS_BLUETOOTH, String.valueOf(features.getHasBluetooth()), bluetoothList);

        List<String> navigationList = featuresOfOthers.stream()
                .map(s -> String.valueOf(s.getHasNavigation()))
                .collect(Collectors.toList());
        compareFeatures(Constants.HAS_NAVIGATION, String.valueOf(features.getHasNavigation()), navigationList);

        List<String> rearCameraList = featuresOfOthers.stream()
                .map(s -> String.valueOf(s.getHasRearCamera()))
                .collect(Collectors.toList());
        compareFeatures(Constants.HAS_REAR_CAMERA, String.valueOf(features.getHasRearCamera()), rearCameraList);
        return comparisonResponse;
    }

    private void compareFeatures(String featureName, String hasFeature, List<String> hasFeatureList) {
        List<FeatureResponse> featureResponses = new ArrayList<>();
        compareAndAddFeature(featureName, hasFeature, hasFeatureList, featureResponses);
        if(comparisonResponse.getFeature() != null)
            featureResponses.addAll(comparisonResponse.getFeature());
        comparisonResponse.setFeature(featureResponses);
    }

    private void compareAndAddFeature(String featureName, String value, List<String> hasFeatureList, List<FeatureResponse> featureResponses) {

        boolean isCommonValue = isCommonType(value, hasFeatureList);

        featureResponses.add(FeatureResponse.builder()
                .name(featureName)
                .values(getAllValuesForType(value, hasFeatureList))
                .isSimilarity(isCommonValue)
                .build());
    }

}

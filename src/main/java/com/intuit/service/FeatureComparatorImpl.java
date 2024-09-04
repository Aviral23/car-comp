package com.intuit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.models.Engine;
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
    public ComparisonResponse compareFeatures(Feature features, List<Feature> features1) {
        ComparisonResponse comparisonResponse = new ComparisonResponse();
        comparisonResponse.setGroupName(Constants.ENGINE);
        List<Engine> engines = features1.stream()
                .map(Feature::getEngine)
                .collect(Collectors.toList());

        compareEngines(features.getEngine(), engines,comparisonResponse);
        return comparisonResponse;
    }

    private void compareEngines(Engine firstEngine, List<Engine> engines, ComparisonResponse comparisonResponse) {
        List<FeatureResponse> featureResponses = new ArrayList<>();

        compareAndAddFeature(Constants.TYPE, firstEngine.getType(), engines, featureResponses);
        compareAndAddFeature(Constants.HORSE_POWER, String.valueOf(firstEngine.getHorsepower()), engines, featureResponses);

        comparisonResponse.setFeature(featureResponses);
    }

    private void compareAndAddFeature(String name, String value, List<Engine> engines, List<FeatureResponse> featureResponses) {
        List<String> values = engines.stream()
                .map(engine -> getValue(name,engine))
                .collect(Collectors.toList());

        boolean isCommonValue = isCommonType(value, values);

        featureResponses.add(FeatureResponse.builder()
                .name(name)
                .values(getAllValuesForType(value, values))
                .isCommonValue(isCommonValue)
                .build());
    }

    public String getValue(String name, Engine engine) {
        String val = "";
        switch (name) {
            case Constants.TYPE:
                val = engine.getType();
                break;
            case Constants.HORSE_POWER:
                val = String.valueOf(engine.getHorsepower());
                break;
            default:
                val = "not found";
                break;
        }
        return val;
    }


}

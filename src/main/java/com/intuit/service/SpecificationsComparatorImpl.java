package com.intuit.service;

import com.intuit.models.Specification;
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
public class SpecificationsComparatorImpl implements SpecificationsComparator{

    private ComparisonResponse comparisonResponse;
    @Override
    public ComparisonResponse compareSpecifications(Specification specificationOne, List<Specification> specificationList) {
        comparisonResponse = new ComparisonResponse();
        comparisonResponse.setGroupName(Constants.SPECIFICATION);
        List<String> seats = specificationList.stream()
                .map(s -> String.valueOf(s.getNumberOfSeats()))
                .collect(Collectors.toList());
        List<String> airbags = specificationList.stream()
                .map(s -> String.valueOf(s.getNumberOfAirbags()))
                .collect(Collectors.toList());
        List<String> warrantyYears = specificationList.stream()
                .map(s -> String.valueOf(s.getWarrantyYears()))
                .collect(Collectors.toList());
        List<String> absList = specificationList.stream()
                .map(s -> String.valueOf(s.getHasABS()))
                .collect(Collectors.toList());
        List<String> adasList = specificationList.stream()
                .map(s -> String.valueOf(s.getHasADAS()))
                .collect(Collectors.toList());

        compareSpecs(Constants.NUMBER_OF_SEATS, String.valueOf(specificationOne.getNumberOfSeats()), seats);
        compareSpecs(Constants.NUMBER_OF_AIRBAGS, String.valueOf(specificationOne.getNumberOfAirbags()), airbags);
        compareSpecs(Constants.WARRANTY_YEARS, String.valueOf(specificationOne.getWarrantyYears()), warrantyYears);
        compareSpecs(Constants.HAS_ABS, String.valueOf(specificationOne.getHasABS()), absList);
        compareSpecs(Constants.HAS_ADAS, String.valueOf(specificationOne.getHasADAS()), adasList);
        return comparisonResponse;
    }

    private void compareSpecs(String name, String numberInCar1, List<String> numbersInOtherCars) {
        List<FeatureResponse> featureResponses = new ArrayList<>();

        compareAndAddFeature(name, numberInCar1, numbersInOtherCars, featureResponses);

        if(comparisonResponse.getFeature() != null)
            featureResponses.addAll(comparisonResponse.getFeature());
        comparisonResponse.setFeature(featureResponses);
    }

    private void compareAndAddFeature(String name, String numberInCar1, List<String> numbersInOtherCars, List<FeatureResponse> featureResponses) {
        boolean isCommonValue = isCommonType(numberInCar1, numbersInOtherCars);

        featureResponses.add(FeatureResponse.builder()
                .name(name)
                .values(getAllValuesForType(numberInCar1, numbersInOtherCars))
                .isSimilarity(isCommonValue)
                .build());
    }

}

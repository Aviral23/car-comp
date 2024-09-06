package com.intuit.service;

import com.intuit.models.Specifications;
import com.intuit.response.ComparisonResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationsComparatorImpl implements SpecificationsComparator{

    @Override
    public ComparisonResponse compareSpecifications(Specifications specificationsOne, List<Specifications> specificationsList) {
        ComparisonResponse comparisonResponse = new ComparisonResponse();
        /*comparisonResponse.setGroupName(Constants.SPECIFICATION);
        List<Integer> seats = specificationsList.stream()
                .map(Specifications::getNumberOfSeats)
                .collect(Collectors.toList());

        compareSeats(specificationsOne.getNumberOfSeats(), seats,comparisonResponse);*/
        return comparisonResponse;
    }

    /*private void compareSeats(Integer seatOne, List<Integer> seats, ComparisonResponse comparisonResponse) {
        List<FeatureResponse> featureResponses = new ArrayList<>();

        compareAndAddFeature(Constants.NUMBER_OF_SEATS, String.valueOf(seatOne.getNumberOfSeats()), seats, featureResponses);
        compareAndAddFeature(Constants.MATERIAL, seatOne.getMaterial(), seats, featureResponses);

        comparisonResponse.setFeature(featureResponses);
    }

    private void compareAndAddFeature(String name, String value, List<Integer> seats, List<FeatureResponse> featureResponses) {
        List<String> values = seats.stream()
                .map(seat -> name.equals(Constants.NUMBER_OF_SEATS) ? String.valueOf(seat.getNumberOfSeats()) : seat.getMaterial())
                .collect(Collectors.toList());

        boolean isCommonValue = isCommonType(value, values);

        featureResponses.add(FeatureResponse.builder()
                .name(name)
                .values(getAllValuesForType(value, values))
                .isCommonValue(isCommonValue)
                .build());
    }*/

}

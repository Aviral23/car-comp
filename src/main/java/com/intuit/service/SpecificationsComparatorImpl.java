package com.intuit.service;

import com.intuit.models.Seats;
import com.intuit.models.Specifications;
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

    @Override
    public ComparisonResponse compareSpecifications(Specifications specificationsOne, List<Specifications> specificationsList) {
        ComparisonResponse comparisonResponse = new ComparisonResponse();
        comparisonResponse.setGroupName(Constants.SPECIFICATION);
        List<Seats> seats = specificationsList.stream()
                .map(Specifications::getSeats)
                .collect(Collectors.toList());

        compareSeats(specificationsOne.getSeats(), seats,comparisonResponse);
        return comparisonResponse;
    }

    private void compareSeats(Seats seatOne, List<Seats> seats, ComparisonResponse comparisonResponse) {
        List<FeatureResponse> featureResponses = new ArrayList<>();

        compareAndAddFeature(Constants.NUMBER_OF_SEATS, String.valueOf(seatOne.getNumberOfSeats()), seats, featureResponses);
        compareAndAddFeature(Constants.MATERIAL, seatOne.getMaterial(), seats, featureResponses);

        comparisonResponse.setFeature(featureResponses);
    }

    private void compareAndAddFeature(String name, String value, List<Seats> seats, List<FeatureResponse> featureResponses) {
        List<String> values = seats.stream()
                .map(seat -> name.equals(Constants.NUMBER_OF_SEATS) ? String.valueOf(seat.getNumberOfSeats()) : seat.getMaterial())
                .collect(Collectors.toList());

        boolean isCommonValue = isCommonType(value, values);

        featureResponses.add(FeatureResponse.builder()
                .name(name)
                .values(getAllValuesForType(value, values))
                .isCommonValue(isCommonValue)
                .build());
    }

}

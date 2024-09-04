package com.intuit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.models.Feature;
import com.intuit.models.Specifications;
import com.intuit.response.ComparisonResponse;

import java.util.ArrayList;
import java.util.List;

public class ComparatorUtils {
    public static List<String> getAllValuesForType(String type, List<String>... valueLists) {
        List<String> allValues = new ArrayList<>();
        allValues.add(type);
        for (List<String> values : valueLists) {
            allValues.addAll(values);
        }
        return allValues;
    }


    public static boolean isCommonType(String type, List<String> allTypes) {
        return allTypes.stream().allMatch(t -> t.equals(type));
    }

}

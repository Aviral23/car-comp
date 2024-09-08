package com.intuit.service;

import java.util.ArrayList;
import java.util.List;

public class ComparatorUtils {
    public static List<String> getAllValuesForType(String type, List<String> valueLists) {
        List<String> allValues = new ArrayList<>();
        allValues.add(type);
        allValues.addAll(valueLists);
        return allValues;
    }

    public static <T> boolean isCommonType(T type, List<T> allTypes) {
        return allTypes.stream().allMatch(t -> t.equals(type));
    }

}

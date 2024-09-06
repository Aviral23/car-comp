package com.intuit.service;

import java.util.ArrayList;
import java.util.List;

public class ComparatorUtils {
    public static <T> List<T> getAllValuesForType(T type, List<T> valueLists) {
        List<T> allValues = new ArrayList<>();
        allValues.add(type);
        allValues.addAll(valueLists);
        return allValues;
    }

    public static <T> boolean isCommonType(T type, List<T> allTypes) {
        return allTypes.stream().allMatch(t -> t.equals(type));
    }

}

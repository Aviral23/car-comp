package com.intuit.service;

import com.intuit.models.Specifications;
import com.intuit.response.ComparisonResponse;

import java.util.List;

public interface SpecificationsComparator {
    ComparisonResponse compareSpecifications(Specifications specificationsOne, List<Specifications> specificationsTwo);

}

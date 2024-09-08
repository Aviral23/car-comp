package com.intuit.service;

import com.intuit.models.Specification;
import com.intuit.response.ComparisonResponse;

import java.util.List;

public interface SpecificationsComparator {
    ComparisonResponse compareSpecifications(Specification specificationOne, List<Specification> specificationTwo);

}

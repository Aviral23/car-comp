package com.intuit.service;

import com.intuit.models.Feature;
import com.intuit.response.ComparisonResponse;

import java.util.List;

public interface FeatureComparator {
      ComparisonResponse compareFeatures(Feature features, List<Feature> features1) ;
    }

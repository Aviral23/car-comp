package com.intuit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.request.CompareRequest;
import com.intuit.response.ComparisonList;

public interface ComparisonLogic {
     ComparisonList compareCars(CompareRequest compareRequest) throws JsonProcessingException;

    }

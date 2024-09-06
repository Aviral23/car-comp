package com.intuit.response;

import lombok.Data;

import java.util.List;

@Data
public class ComparisonResponse {
    private List<FeatureResponse> feature;
    private String groupName;
}

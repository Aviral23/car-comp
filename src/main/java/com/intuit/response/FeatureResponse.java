package com.intuit.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeatureResponse {
    private String name;
    private List<Boolean> values;
    private boolean isCommonValue;
}

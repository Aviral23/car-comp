package com.intuit.response;

import com.intuit.models.Feature;
import com.intuit.models.Specifications;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class ComparisonResponse {
    private List<FeatureResponse> feature;
    private String groupName;
}

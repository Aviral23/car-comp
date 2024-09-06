package com.intuit.models;

import lombok.Data;

@Data
public class Safety {
    private Boolean hasAirbags;
    private Boolean hasABS;

    public Safety(Boolean hasAirbags, Boolean hasABS) {
        this.hasAirbags = hasAirbags;
        this.hasABS = hasABS;
    }
}

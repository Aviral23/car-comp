package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Safety implements Serializable {
    private boolean hasAirbags;
    private boolean hasABS;
}

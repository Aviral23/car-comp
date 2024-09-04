package com.intuit.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Colors implements Serializable {
    private String color;
    private Images images;
}

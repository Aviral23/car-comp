package com.intuit.models;


import lombok.Data;

import java.io.Serializable;

@Data
public class Feature implements Serializable {
    private Engine engine;
    private Transmission transmission;
    private Dimension dimension;

}


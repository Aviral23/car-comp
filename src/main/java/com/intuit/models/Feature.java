package com.intuit.models;


import lombok.Data;

import java.io.Serializable;

@Data
public class Feature implements Serializable {
    private Boolean hasNavigation;
    private Boolean hasBluetooth;
    private Boolean hasRearCamera;
    private GearTransmission gearTransmission;
    private Dimension dimension;

    public Feature() {
    }

    public Feature(Boolean hasNavigation, Boolean hasBluetooth, Boolean hasRearCamera, GearTransmission gearTransmission, Dimension dimension) {
        this.hasNavigation = hasNavigation;
        this.hasBluetooth = hasBluetooth;
        this.hasRearCamera = hasRearCamera;
        this.gearTransmission = gearTransmission;
        this.dimension = dimension;
    }
}


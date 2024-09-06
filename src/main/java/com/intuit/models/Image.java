package com.intuit.models;


import lombok.Data;

import java.io.Serializable;

@Data
public class Image implements Serializable {
    private String image;

    public Image(String image) {
        this.image = image;
    }
}


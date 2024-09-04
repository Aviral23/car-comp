package com.intuit.monitoring;


import lombok.Data;

@Data
public class DataDogEntity {
    String requestApi;
    long elapsedTime;

    Integer httpStatus;

    String requestType;
}

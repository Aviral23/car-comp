package com.intuit.monitoring;

import lombok.Data;

@Data
public class DataDogExceptionEntity {
    private String exceptionName;
    private Integer httpCode;
    private String requestApi;
}

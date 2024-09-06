package com.intuit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("car")
public class HealthCheckController {

    @GetMapping(value = "/health")
    public HttpStatus healthCheck(){
        return HttpStatus.OK;
    }
}

package com.intuit.filter;

import com.intuit.monitoring.DataDogEntity;
import com.intuit.monitoring.PrometheusMetricPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Autowired
    private PrometheusMetricPublisher prometheusMetricPublisher;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String uri = httpServletRequest.getRequestURI();
        int httpStatus = 500;

        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            httpStatus = httpServletResponse.getStatus();
        } catch (Exception e) {
            LOGGER.error("Exception occurred: {}", e.getMessage());
            throw e;
        } finally {
            long elapsed = System.currentTimeMillis() - startTime;
            setDataDogMetrics(uri, httpStatus, elapsed);
        }
    }

    private void setDataDogMetrics(String uri, int httpStatus, long elapsed) {
        try {
            DataDogEntity dataDogEntity = new DataDogEntity();
            dataDogEntity.setElapsedTime(elapsed);
            dataDogEntity.setRequestApi(uri);
            dataDogEntity.setHttpStatus(httpStatus);

            publishDataDogMetric(dataDogEntity);
        } catch (Exception ex) {
            LOGGER.error("Error setting DataDog metrics: {}", ex.getMessage());
        }
    }

    private void publishDataDogMetric(DataDogEntity dataDogEntity) {
        try {
            prometheusMetricPublisher.incrementApiCount(dataDogEntity);
            prometheusMetricPublisher.recordExecutionTimeOfApi(dataDogEntity);
        } catch (Exception ex) {
            LOGGER.error("Error publishing DataDog metrics: {}", ex.getMessage());
        }
    }
}

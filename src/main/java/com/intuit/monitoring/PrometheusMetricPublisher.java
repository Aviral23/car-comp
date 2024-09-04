package com.intuit.monitoring;

import com.intuit.monitoring.DataDogEntity;
import com.intuit.monitoring.DataDogExceptionEntity;
import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import org.springframework.beans.factory.annotation.Value;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class PrometheusMetricPublisher {
    private StatsDClient metricClient;

    @Value("${prometheus.namespace}")
    private String PROMETHEUS_PREFIX;

    @Value("${prometheus.hostname}")
    private String PROMETHEUS_HOSTNAME;

    @Value("${prometheus.port}")
    private String PROMETHEUS_PORT;

    @Autowired
    Environment environment;

    @PostConstruct
    public void init(){
        boolean isProductionProfile = false;

        for(final String profileName:environment.getActiveProfiles()){
            if("production".equals(profileName)){
                isProductionProfile = true;
            }
            String hostName = getHostName();
            if(isProductionProfile) {
                metricClient = new NonBlockingStatsDClient(PROMETHEUS_PREFIX, PROMETHEUS_HOSTNAME, Integer.parseInt(PROMETHEUS_PORT),"application"+PROMETHEUS_PREFIX,"hostname:"+hostName);
            }
            else {
                metricClient = new NoOpStatsDClient();
            }

        }
    }

    public void recordExecutionTimeOfApi(DataDogEntity dataDogEntity){
        metricClient.recordHistogramValue("api_execution_time", dataDogEntity.getElapsedTime(),"api_name:"+dataDogEntity.getRequestApi());
    }

    public void incrementApiCount(DataDogEntity dataDogEntity){
        metricClient.increment("api_count","api_name:"+dataDogEntity.getRequestApi()+"httpCode:"+dataDogEntity.httpStatus);
    }

    public void incrementExceptionCount(DataDogExceptionEntity dataDogEntity){
        metricClient.increment("exception_count","api_name:"+dataDogEntity.getRequestApi()+"httpCode:"+dataDogEntity.getHttpCode(),"exception_name:"+dataDogEntity.getExceptionName());
    }
    private String getHostName() {
        String hostName = "unKnownHost";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex){

        }
        return hostName;
    }


}

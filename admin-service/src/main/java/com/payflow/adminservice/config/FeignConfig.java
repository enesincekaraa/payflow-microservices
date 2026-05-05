package com.payflow.adminservice.config;

import feign.Client;
import feign.hc5.ApacheHttp5Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Client feignClient() {
        return new ApacheHttp5Client();
    }
}

package com.payflow.paymentservice.infrastructure.iyzico;

import com.iyzipay.Options;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "iyzico")
@Setter
public class IyzicoConfig {

    private String apiKey;
    private String secretKey;
    private String baseUrl;

    @Bean
    public Options iyzicoOptions() {
        Options options = new Options();
        options.setApiKey(apiKey);
        options.setSecretKey(secretKey);
        options.setBaseUrl(baseUrl);
        return options;
    }
}
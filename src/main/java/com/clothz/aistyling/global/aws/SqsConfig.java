package com.clothz.aistyling.global.aws;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsConfig {

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
    @Bean
    public SqsTemplate sqsTemplate() {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }
}
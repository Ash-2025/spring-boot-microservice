package com.ash.spring_boot_microservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ImageProcess(){
        Map<String,String> configs = new HashMap<>();
        configs.put("retention.ms", "3600");
        return TopicBuilder.name("ImageProcess")
                .partitions(3)
                .replicas(1)
                .configs(configs)
                .build();
    }
}

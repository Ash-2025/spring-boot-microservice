package com.ash.spring_boot_microservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "ImageProcess";

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendJobs(String jobData){
        try{
            var res = kafkaTemplate.send(TOPIC, jobData).get();
            log.info("Job produced Successfully {}", res);
        }catch (Exception e){
            log.error("Failed to produce job {}", jobData, e);
            throw new RuntimeException("Failed to send kafka message",e);
        }
    }
}

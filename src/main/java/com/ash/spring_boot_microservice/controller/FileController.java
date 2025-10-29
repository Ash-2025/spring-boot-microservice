package com.ash.spring_boot_microservice.controller;

import com.example.demo.Dto.UploadRequest;
import com.example.demo.services.KafkaProducer;
import com.example.demo.services.S3Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final S3Service s3Service;
    private final KafkaProducer kafkaProducer;
    private final JedisPool jedisPool;

    private String createJobData(UploadRequest data){
        return "";
    }

    public FileController(S3Service s3Service, KafkaProducer kafkaProducer, JedisPool jedisPool){
        this.s3Service = s3Service;
        this.kafkaProducer = kafkaProducer;
        this.jedisPool = jedisPool;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> getUploadURL(@RequestBody UploadRequest req){
        String url = s3Service.generateUploadURL(
                req.getTaskName(),
                req.getUuid(),
                req.getOriginalName(),
                req.getContentType()
        );
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }

    @PostMapping("/process")
    public ResponseEntity<String> init(@RequestBody UploadRequest req) throws JsonProcessingException {
        // call the kafka client and produce a job
        String jobData = createJobData(req);
        String key = s3Service.buildObjectKey(
                req.getTaskName(),
                req.getUuid(),
                req.getOriginalName().contains(".") ?
                req.getOriginalName().substring(req.getOriginalName().lastIndexOf(".") + 1) : "bin"
        );
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> taskData = mapper.convertValue(req.getTaskData(),Map.class);

        key = key.replaceFirst("^test-bucket/","");
        taskData.put("key",key);
        taskData.put("taskName",req.getTaskName());
        taskData.put("originalName", req.getOriginalName());
        taskData.put("uuid", req.getUuid());
        String jsonString = mapper.writeValueAsString(taskData);
        kafkaProducer.sendJobs(jsonString);
        //if job creation is successful then send a 200 status
        return ResponseEntity.ok("Job added" + jsonString);
    }

    @GetMapping("/getfile")
    public ResponseEntity<String> getDownloadURL(@RequestParam String uuid){
        String url;
        try(Jedis jedis = jedisPool.getResource()){
            url =  jedis.get(uuid);
        }
        if(url.isEmpty()){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find URL");
        }
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }
}

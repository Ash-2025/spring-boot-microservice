package com.ash.spring_boot_microservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class S3Service {
    private final S3Presigner presigner;
    private final String bucket;
    private final long preSignerSeconds;

    public S3Service(S3Presigner presigner,
                     @Value("${app.s3.bucket-name}") String bucket,
                     @Value("${app.s3.presign-duration-seconds}") long preSignerSeconds)
    {
        this.presigner = presigner;
        this.bucket = bucket;
        this.preSignerSeconds = preSignerSeconds;
    }

    // Generate key for file path
    public String buildObjectKey(String task, String uuid, String ext){
        return String.format("%s/%s/%s.%s", task, uuid, uuid, ext);
    }

    public String generateUploadURL(String task, String uuid, String originalName, String contentType){
        String ext = originalName.contains(".") ?
                originalName.substring(originalName.lastIndexOf(".") + 1) : "bin";

        String key = buildObjectKey(task,uuid,ext);

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType == null ? "application/octet-stream" : contentType)
                .build();

        PutObjectPresignRequest preSignReq = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(preSignerSeconds))
                .putObjectRequest(putReq)
                .build();

        PresignedPutObjectRequest presigned = presigner.presignPutObject(preSignReq);

        return presigned.url().toString();
    }
}

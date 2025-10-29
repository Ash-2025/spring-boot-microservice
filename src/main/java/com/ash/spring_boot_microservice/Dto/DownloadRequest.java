package com.ash.spring_boot_microservice.Dto;

import lombok.Data;

@Data
public class DownloadRequest {
    private String taskName;
    private String uuid;
    private String originalName;
}

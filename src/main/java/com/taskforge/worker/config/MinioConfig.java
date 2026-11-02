package com.taskforge.worker.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    @Value("${taskforge.minio.endpoint:http://minio:9000}")
    private String endpoint;

    @Value("${taskforge.minio.access-key:taskforgeadmin}")
    private String accessKey;

    @Value("${taskforge.minio.secret-key:taskforgepassword}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}

package io.github.yxsnake.pisces.file.configuration;

import cn.hutool.core.lang.Assert;
import io.github.yxsnake.pisces.file.configuration.properties.MinioProperties;
import io.github.yxsnake.pisces.file.service.FileService;
import io.github.yxsnake.pisces.file.service.impl.MinioServiceImpl;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({MinioProperties.class})
@RequiredArgsConstructor
@Configuration
public class MinioAutoConfiguration {

    private final MinioProperties minioProperties;

    @Bean
    @ConditionalOnProperty(value = "minio.enabled", havingValue = "true", matchIfMissing = true)
    public MinioClient minioClient(){
        Assert.notBlank(minioProperties.getEndpoint(), "MinIO endpoint can not be null");
        Assert.notBlank(minioProperties.getAccessKey(), "MinIO accessKey can not be null");
        Assert.notBlank(minioProperties.getSecretKey(), "MinIO secretKey can not be null");
        Assert.notBlank(minioProperties.getBucketName(), "MinIO bucketName can not be null");
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(),minioProperties.getSecretKey())
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "minio.enabled", havingValue = "true", matchIfMissing = true)
    public FileService fileService(){
        return new MinioServiceImpl(minioProperties,minioClient());
    }
}

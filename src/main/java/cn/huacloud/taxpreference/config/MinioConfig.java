package cn.huacloud.taxpreference.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tax-preference.minio")
public class MinioConfig {

    /**
     * 服务地址
     */
    private String endpoint;
    /**
     * 客户端账号
     */
    private String accessKey;
    /**
     * 客户端秘钥
     */
    private String secretKey;
    /**
     * 存储桶
     */
    private String bucket = "tax-preference";

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        // 检查存储桶是否已经创建，没有创建则自动创建
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }

        return minioClient;
    }
}

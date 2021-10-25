package cn.huacloud.taxpreference.config;

import io.minio.*;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;

/**
 * @author wangkh
 */
public class MinioClientTest {

   static MinioClient minioClient;

    @BeforeClass
    public static void beforeClass() throws Exception {
        minioClient = MinioClient.builder()
                .endpoint("http://172.16.122.17:9000")
                .credentials("tax_preference", "tax_preference")
                .build();
    }

    @Test
    public void testUploadFile() throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("tax-preference").build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("tax-preference").build());
        }

        ClassPathResource resource = new ClassPathResource("application.yml");

        byte[] bytes = IOUtils.toByteArray(resource.getInputStream());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        minioClient.putObject(PutObjectArgs.builder()
                .bucket("tax-preference")
                .object("application.yml")
                // 不设置objectSize就要设置partSize二选一
                .stream(inputStream, bytes.length, -1)
                .build());
    }
}

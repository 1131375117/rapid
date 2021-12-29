package cn.huacloud.taxpreference.config;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;

/**
 * @author wangkh
 */
@Slf4j
public class MinioClientTest {

   static MinioClient minioClient;

   static MinioClient spiderMinioClient;

    @BeforeClass
    public static void beforeClass() throws Exception {
        minioClient = MinioClient.builder()
                .endpoint("http://172.16.122.17:9000")
                .credentials("tax_preference", "tax_preference")
                .build();

        spiderMinioClient = MinioClient.builder()
                .endpoint("http://172.16.18.28:9000")
                .credentials("data_spider", "data_spider")
                .build();
    }

    @Test
    public void testCopy() throws Exception {
        String name = "www.chinatax.gov.cn/chinatax/n371/c206822/5109427/files/9340.doc.docx";
        GetObjectResponse object = spiderMinioClient.getObject(GetObjectArgs.builder().bucket("data-spider").object(name).build());
        Headers headers = object.headers();
        String contentLength = headers.get("Content-Length");
        minioClient.putObject(PutObjectArgs.builder().bucket("tax-preference")
                .object("spider/" + name)
                .stream(object, Long.parseLong(contentLength), -1)
                .build());
        log.info("{}", headers);
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

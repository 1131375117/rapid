package cn.huacloud.taxpreference.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;

@Slf4j
public class PasswordSecureUtilTest {

    @Test
    public void decrypt() throws Exception {
        String data = IOUtils.toString(new ClassPathResource("rsa/test_data_02.txt").getInputStream(), StandardCharsets.UTF_8);
        String decrypt = PasswordSecureUtil.decrypt(data);
        log.info(decrypt);
    }
}
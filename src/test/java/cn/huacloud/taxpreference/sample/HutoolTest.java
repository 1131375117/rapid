package cn.huacloud.taxpreference.sample;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.UUID;

/**
 * @author wangkh
 */
@Slf4j
public class HutoolTest {

    @Test
    public void testAes() {
        SecretKey secretKey = KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue());
        String aesKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("ASE Key: " + aesKey);
        AES aes = SecureUtil.aes(Base64.getDecoder().decode(aesKey));
        String encryptBase64 = aes.encryptBase64("薪福通");
        System.out.println("Encrypt: " + encryptBase64);
        String decryptStr = aes.decryptStr(encryptBase64);
        System.out.println(decryptStr);
    }

    @Test
    public void testBase64() {
        AES aes = SecureUtil.aes(Base64.getDecoder().decode("wTCZPC83urDeGCzOm/fh8Q=="));
        String value = aes.decryptStr("PXXRQotcpRXGOy2Xf7lsBg==");
        System.out.println(value);
        System.out.println(aes.encryptBase64("薪福通"));
        for (int i = 0; i < 10; i++) {
            System.out.println(aes.encryptBase64(UUID.randomUUID().toString()));
        }
    }
}

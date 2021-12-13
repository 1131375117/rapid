package cn.huacloud.taxpreference.sample;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesSearchQueryDTO;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单测试类
 * @author wangkh
 */
@Slf4j
public class SampleTest {

    @Test
    public void testRsaKeyPrint() {
        RSA rsa = new RSA();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        log.info(privateKeyBase64);
        log.info(publicKeyBase64);
    }

    @Test
    public void testRsaKey() {
        RSA rsa = new RSA();
        PrivateKey privateKey = rsa.getPrivateKey();
        PublicKey publicKey = rsa.getPublicKey();
        String privateKeyStr = Base64.encodeStr(privateKey.getEncoded(), false, false);
        String publicKeyStr = Base64.encodeStr(publicKey.getEncoded(), false, false);
        RSA testRsa = new RSA(privateKeyStr, publicKeyStr);
        log.info(privateKeyStr);
        log.info(publicKeyStr);
    }

    @Test
    public void testRsa() throws Exception {
        String privateKey = readRsaKey("rsa/id_rsa");
        String publicKey = readRsaKey("rsa/id_rsa.pub");
        RSA rsa = new RSA(privateKey, publicKey);
        byte[] bytes = rsa.encrypt("测试", KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(bytes, KeyType.PrivateKey);
        log.info(StrUtil.str(decrypt, StandardCharsets.UTF_8));
        String data = readRsaKey("rsa/test_data.txt");
        String str = rsa.decryptStr(data, KeyType.PrivateKey);
        log.info(str);
    }

    private String readRsaKey(String path) throws Exception {
        Optional<String> key = IOUtils.readLines(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8)
                .stream()
                .filter(line -> !line.startsWith("----"))
                .map(line -> line.replaceAll("\n", ""))
                .reduce((a, b) -> a + b);
        return key.get();
    }

    @Test
    public void testSnowFlake() {
        Snowflake snowflake = new Snowflake();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < 1000; i++) {
            String id = snowflake.nextId() + "";
            String uid = id.substring(id.length() - 8);
            map.put(id, uid);
        }
        Set<String> set = new LinkedHashSet<>();
        Set<String> exSet = new LinkedHashSet<>();
        for (String value : map.values()) {
            if (set.contains(value)) {
                exSet.add(value);
            } else {
                set.add(value);
            }
        }
        log.info("");
    }

    @Test
    public void testMD5() {
        String password = SaSecureUtil.md5("123456");
        log.info(password);
    }

    @Test
    public void snowFlake() {
        Snowflake snowflake = new Snowflake();
        long nextId = snowflake.nextId();
        log.info("nextId: {}", nextId);
    }

    @Test
    public void testGuavaCache() throws Exception {
        Cache<Object, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build();

        String one = cache.get(this, () -> "ONE");
        String tow = cache.get(this, () -> "TOW");
        // cache.cleanUp();
        cache.invalidateAll();
        String three = cache.get(this, () -> "THREE");
        log.info("one: {}", one);
        log.info("tow: {}", tow);
        log.info("three: {}", three);
    }

    @Test
    public void testSuper() {
        PoliciesSearchQueryDTO queryDTO = new PoliciesSearchQueryDTO();
        queryDTO.setAreaCode("");
        queryDTO.setDocCode("");
        queryDTO.paramReasonable();
        log.info("{}", queryDTO);
    }

    @Test
    public void htmlTest() {
        LocalDate now = LocalDate.now();
        log.info(now.toString());
        log.info(LocalDateTime.now().toString());
    }

    @Test
    public void testPassword() {
        String patternStr = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,30}";
        Pattern pattern = Pattern.compile(patternStr);
        String password1 = "12345678Aa";
        String password2 = "12345678Aa!";

        Matcher matcher1 = pattern.matcher(password1);
        boolean find1 = matcher1.find();
        log.info("{} <=> {}", password1, find1);

        Matcher matcher2 = pattern.matcher(password2);
        boolean find2 = matcher2.find();
        log.info("{} <=> {}", password2, find2);
    }

    @Test
    public void testStreamFirst() {
        List<String> list = new ArrayList<>();
        Optional<String> first = list.stream().findFirst();
        log.info("isPresent: {}", first.isPresent());
    }

    @Test
    public void pinyin4jTest() throws Exception {
        String target = "选择增值税-一般纳税人";
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        StringBuilder sb = new StringBuilder();
        for (char ch : target.toCharArray()) {
            String[] strings = PinyinHelper.toHanyuPinyinStringArray(ch, format);
            if (strings == null) {
                sb.append(ch);
            } else {
                sb.append(strings[0].substring(0, 1));
            }
        }
        log.info(sb.toString());
    }

    @Test
    public void testStringJoin() {
        String join = String.join(",", new ArrayList<>());
        log.info("join: '{}'", join);
    }

    @Test
    public void testIdWorker() {
        for (int i = 0; i < 100; i++) {
            log.info(IdWorker.get32UUID());
        }
    }

    @Test
    public void testReflectMethod() {
        Method method = this.getClass().getDeclaredMethods()[0];
        log.info(method.toString());
        log.info(method.toGenericString());
    }

    @Test
    public void md5Test() {
        String md5 = SaSecureUtil.md5("123456");
        log.info(md5);
    }

    @Test
    public void testLogMessageFormat() {
        log.info("{}和{}是好友。", "小明", "小红");
    }
}

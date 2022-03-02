package cn.huacloud.taxpreference.services.wwx.ase;

import cn.huacloud.taxpreference.config.WWXConfig;
import cn.huacloud.taxpreference.services.wwx.constants.ReceiveIdGetter;
import com.ctc.wstx.shaded.msv_core.verifier.regexp.StringCareLevelCalculator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.sun.java.accessibility.util.AccessibilityListenerList;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.units.qual.C;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.json.YamlJsonParser;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MessageHelperTest {

    static MessageHelperFactory messageHelperFactory;

    static XmlMapper xmlMapper;

    @Test
    public void testXml() throws Exception {
        String xml = readClasspathString("xml/data_callback.xml");
        JsonNode root = xmlMapper.readTree(xml);
        boolean flag = root.has("ToUserName");
        JsonNode toUserName = root.get("ToUserName");
        System.out.println(root.toPrettyString());
    }

    @Test
    public void verifyURL() throws Exception {
    }

    @Test
    public void decrypt() {
    }

    @Test
    public void verifySignature() {
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        WWXConfig.App wwxConfig = new WWXConfig.App();
        wwxConfig.setCorpId("wx9a124e5ac3e8815c");
        wwxConfig.setProviderSecret("-2siEGlPSyf5EeOk3g61TxgD9DwxazfF3MwQXFo9KygK6ijP9Rweh7IsXCGRQb9E");
        wwxConfig.setSuiteId("wxc56593939fb029d1");
        wwxConfig.setSecret("dcBNFmhW7-iGXntd3RufqlY3u228-H0HbZBX1pen6_Q");
        wwxConfig.setToken("Pr0KgCr7qor43O2yKLG6Ah8sce9JrLom");
        wwxConfig.setEncodingAesKey("85kZ3CSeTdxQjHqntw5scxk1pLJrmx2psoIReB1ySv1");
        xmlMapper = new XmlMapper();
    }

    private String readClasspathString(String path) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return IOUtils.toString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
    }
}
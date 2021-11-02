package cn.huacloud.taxpreference.sample;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 简单测试类
 * @author wangkh
 */
@Slf4j
public class SampleTest {

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

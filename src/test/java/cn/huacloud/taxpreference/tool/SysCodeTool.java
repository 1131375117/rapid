package cn.huacloud.taxpreference.tool;

import cn.huacloud.taxpreference.common.enums.SysCodeStatus;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import cn.hutool.core.convert.Convert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统码值工具类
 *
 * @author wangkh
 */
@Slf4j
public class SysCodeTool /*extends BaseApplicationTest*/ {

    @Test
    public void generateSysCode() {
        List<SysCodeProvider> sysCodeProviders = Arrays.stream(SysCodeTool.class.getDeclaredFields())
                .filter(field -> field.getType().isAssignableFrom(SysCodeProvider.class))
                .map(field -> {
                    try {
                        return (SysCodeProvider) field.get(this);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 执行fetch方法获取码值集合
        List<SysCodeDO> allSysCodes = new ArrayList<>();
        for (SysCodeProvider sysCodeProvider : sysCodeProviders) {
            List<SysCodeDO> sysCodeDOS = sysCodeProvider.fetch((long) (allSysCodes.size() + 1));
            allSysCodes.addAll(sysCodeDOS);
        }

        // 统一设置码值
        Map<String, SysCodeDO> map = new LinkedHashMap<>();
        for (SysCodeDO sysCodeDO : allSysCodes) {
            if (sysCodeDO.getCodeValue() != null) {
                continue;
            }
            String codeValue = sysCodeDO.getCodeType().getValue() + "_" + toFistLetterPinyin(sysCodeDO.getCodeName());
            sysCodeDO.setCodeValue(codeValue);
            if (map.containsKey(codeValue)) {
                log.error("系统码值重复：{} <-> {} {}", map.get(codeValue).getCodeName() , sysCodeDO.getCodeName(), codeValue);
            } else {
                map.put(codeValue, sysCodeDO);
            }
        }

        log.info("系统码值生成完毕");
    }

    /**
     * 系统码值提供器
     */
    interface SysCodeProvider {
        List<SysCodeDO> fetch(Long nextId);
    }

    /**
     * 所属税种
     */
    private SysCodeProvider taxCategories = nextId -> readSingleFile("所属税种.txt", SysCodeType.TAX_CATEGORIES, nextId);

    /**
     * 所属区域
     */
    private SysCodeProvider area = nextId -> {
        return new ArrayList<>();
    };

    /**
     * 适用行业
     */
    private SysCodeProvider industry = nextId -> {
        return new ArrayList<>();
    };

    /**
     * 纳税人资格认定类型
     */
    private SysCodeProvider taxpayerIdentifyType = nextId -> readSingleFile("纳税人资格认定类型.txt", SysCodeType.TAXPAYER_IDENTIFY_TYPE, nextId);

    /**
     * 适用企业类型
     */
    private SysCodeProvider enterpriseType = nextId -> new ArrayList<>();

    /**
     * 纳税人登记注册类型
     */
    private SysCodeProvider taxpayerRegisterType = nextId -> readSingleFile("纳税人登记注册类型.txt", SysCodeType.TAXPAYER_REGISTER_TYPE, nextId);

    /**
     * 纳税人类型
     */
    private SysCodeProvider taxpayerType = nextId -> readSingleFile("纳税人类型.txt", SysCodeType.TAXPAYER_TYPE, nextId);

    public List<SysCodeDO> readSingleFile(String fileName, SysCodeType sysCodeType, Long nextId) {
        try {
            InputStream inputStream = new ClassPathResource("sys_code/" + fileName).getInputStream();
            List<String> list = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            List<SysCodeDO> sysCodeDOList = new ArrayList<>();
            for (String name : list) {
                SysCodeDO sysCodeDO = new SysCodeDO();
                sysCodeDO.setCodeName(name)
                        .setId(nextId)
                        .setPid(0L)
                        .setCodeType(sysCodeType)
                        .setCodeStatus(SysCodeStatus.VALID)
                        .setLeaf(true)
                        .setSort(nextId);
                sysCodeDOList.add(sysCodeDO);
                nextId++;
            }
            return sysCodeDOList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toFistLetterPinyin(String name) {
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            StringBuilder sb = new StringBuilder();
            for (char ch : name.toCharArray()) {
                String[] strings = PinyinHelper.toHanyuPinyinStringArray(ch, format);
                if (strings == null) {
                    sb.append(Convert.toDBC(ch + ""));
                } else {
                    sb.append(strings[0].substring(0, 1));
                }
            }
            return sb.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new RuntimeException(e);
        }
    }
}

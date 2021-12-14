package cn.huacloud.taxpreference.tool;

import cn.huacloud.taxpreference.BaseApplicationTest;
import cn.huacloud.taxpreference.common.enums.SysCodeStatus;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import cn.huacloud.taxpreference.services.common.mapper.SysCodeMapper;
import cn.huacloud.taxpreference.tool.SysCodeTool.IgnoreProvider;
import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统码值工具类
 *
 * @author wangkh
 */
@Ignore
@Slf4j
public class SysCodeTool extends BaseApplicationTest {

    @Autowired
    SysCodeMapper sysCodeMapper;

    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void generateSysCode() throws Exception {
        List<SysCodeProvider> sysCodeProviders = Arrays.stream(SysCodeTool.class.getDeclaredFields())
                .filter(field -> field.getType().isAssignableFrom(SysCodeProvider.class)
                        && !field.isAnnotationPresent(IgnoreProvider.class))
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
                log.error("系统码值重复：{} <-> {} {}", map.get(codeValue).getCodeName(), sysCodeDO.getCodeName(), codeValue);
            } else {
                map.put(codeValue, sysCodeDO);
            }
        }

        log.info("系统码值生成完毕");

        sysCodeMapper.delete(null);

        for (SysCodeDO sysCodeDO : allSysCodes) {
            sysCodeMapper.insert(sysCodeDO);
        }

        log.info("系统码值导入完毕，一共导入{}条", allSysCodes.size());
    }

    /**
     * 系统码值提供器
     */
    interface SysCodeProvider {

        List<SysCodeDO> fetch(Long nextId) throws Exception;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface IgnoreProvider {

    }

    /**
     * 所属税种
     */
    private SysCodeProvider taxCategories = nextId -> readSingleFile("所属税种.txt", SysCodeType.TAX_CATEGORIES, nextId);

    /**
     * 所属区域
     */
    private SysCodeProvider area = nextId -> {
        List<Area> standardAreas = objectMapper.readValue(new ClassPathResource("sys_code/所属区域.json").getInputStream(),
                new TypeReference<List<Area>>() {
                });
        List<Area> targetArea = new ArrayList<>();
        Area zy = new Area().setName("中央")
                .setPid(0L);
        Area df = new Area().setName("地方（各省-市）")
                .setPid(0L)
                .setCityList(standardAreas);
        targetArea.add(zy);
        targetArea.add(df);
        List<SysCodeDO> list = new ArrayList<>();
        forEachArea(list, targetArea, 0L, nextId, "");
        return list;
    };

    private Long forEachArea(List<SysCodeDO> list, List<Area> areas, Long pid, Long nextId, String pName) {
        // 直辖市处理
        if (areas.size() == 1 && pName.equals(areas.get(0).getName())) {
            return nextId;
        }

        for (Area area : areas) {

            SysCodeDO sysCodeDO = new SysCodeDO();
            String codeValue = area.getCode() == null ? null : SysCodeType.AREA.getValue() + "_" + area.getCode();
            sysCodeDO.setCodeName(area.getName())
                    .setCodeValue(codeValue)
                    .setId(nextId)
                    .setPid(pid)
                    .setCodeType(SysCodeType.AREA)
                    .setCodeStatus(SysCodeStatus.VALID)
                    .setSort(nextId);
            list.add(sysCodeDO);
            nextId++;
            List<Area> cityList = area.getCityList();
            if (cityList != null) {
                sysCodeDO.setLeaf(false);
                nextId = forEachArea(list, cityList, sysCodeDO.getId(), nextId, sysCodeDO.getCodeName());
            } else {
                sysCodeDO.setLeaf(true);
            }
        }
        return nextId;
    }

    /**
     * 适用行业
     */
    private SysCodeProvider csvIndustry = nextId -> {
        InputStream inputStream = new ClassPathResource("sys_code/行业代码.csv").getInputStream();

        // 使用指定的字符编码以字符串列表的形式获取InputStream的内容，每行一个条目
        List<String> list = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);

        Long currentPid = 0L;
        List<SysCodeDO> sysCodeDOList = new ArrayList<>();

        // 删除.csv第一行
        list.remove(0);
        for (String line : list) {
            String[] split = line.split(",");

            String code = split[6];
            String name = split[1];
            String validStatus = split[8];

            Long pid;
            boolean isLeaf;
            if (code.length() == 1) {
                pid = 0L;
                currentPid = nextId;
                isLeaf = false;
            } else {
                pid = currentPid;
                isLeaf = true;
            }

            SysCodeDO sysCodeDO = new SysCodeDO();
            if (validStatus.contains("Y")) {
                sysCodeDO.setCodeStatus(SysCodeStatus.VALID);
            }
            sysCodeDO.setCodeName(name)
                    .setCodeValue(code)
                    .setId(nextId)
                    .setPid(pid)
                    .setCodeType(SysCodeType.INDUSTRY)
                    .setLeaf(isLeaf)
                    .setSort(nextId);


            nextId++;
            sysCodeDOList.add(sysCodeDO);
        }
        return sysCodeDOList;

    };

    @IgnoreProvider
    private SysCodeProvider industry = nextId -> {
        InputStream inputStream = new ClassPathResource("sys_code/适用行业.txt").getInputStream();
        List<String> list = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);

        Long currentPid = 0L;
        List<SysCodeDO> sysCodeDOList = new ArrayList<>();
        // 添加不限码值
        SysCodeDO unlimited = new SysCodeDO().setCodeName("不限")
                .setCodeValue(SysCodeType.INDUSTRY.getValue() + "_" + "BX")
                .setId(nextId)
                .setPid(0L)
                .setCodeType(SysCodeType.INDUSTRY)
                .setCodeStatus(SysCodeStatus.VALID)
                .setLeaf(true)
                .setSort(nextId);
        sysCodeDOList.add(unlimited);
        nextId++;

        for (String line : list) {
            line = line.replaceAll("‥", " ");
            String[] split = line.split(" ");
            String code = split[0];
            String name = split[1];

            Long pid;
            boolean isLeaf;
            if (code.length() == 1) {
                pid = 0L;
                currentPid = nextId;
                isLeaf = false;
            } else {
                pid = currentPid;
                isLeaf = true;
            }

            SysCodeDO sysCodeDO = new SysCodeDO().setCodeName(name)
                    .setCodeValue(SysCodeType.INDUSTRY.getValue() + "_" + code)
                    .setId(nextId)
                    .setPid(pid)
                    .setCodeType(SysCodeType.INDUSTRY)
                    .setCodeStatus(SysCodeStatus.VALID)
                    .setLeaf(isLeaf)
                    .setSort(nextId);

            nextId++;
            sysCodeDOList.add(sysCodeDO);
        }
        return sysCodeDOList;
    };

    /**
     * 纳税人资格认定类型
     */
    private SysCodeProvider taxpayerIdentifyType = nextId -> readSingleFile("纳税人资格认定类型.txt",
            SysCodeType.TAXPAYER_IDENTIFY_TYPE, nextId);

    /**
     * 适用企业类型
     */
    private SysCodeProvider enterpriseType = nextId -> readSingleFile("适用企业类型.txt", SysCodeType.ENTERPRISE_TYPE,
            nextId);

    /**
     * 纳税人登记注册类型
     */
    private SysCodeProvider taxpayerRegisterType = nextId -> readSingleFile("纳税人登记注册类型.txt",
            SysCodeType.TAXPAYER_REGISTER_TYPE, nextId);

    /**
     * 纳税人类型
     */
    private SysCodeProvider taxpayerType = nextId -> {
        List<SysCodeDO> sysCodeDOList = readSingleFile("纳税人类型.txt", SysCodeType.TAXPAYER_TYPE, nextId);
        // TAX_CATEGORIES_ZZS、TAX_CATEGORIES_QYSDS
        for (SysCodeDO sysCodeDO : sysCodeDOList) {
            String codeName = sysCodeDO.getCodeName();
            if (codeName.startsWith("增值税")) {
                sysCodeDO.setCodeName(StringUtils.substringAfter(codeName, "-"));
                sysCodeDO.setExtendsField1("TAX_CATEGORIES_ZZS");
                sysCodeDO.setExtendsField2("增值税");
            } else if (codeName.startsWith("企业所得税")) {
                sysCodeDO.setCodeName(StringUtils.substringAfter(codeName, "-"));
                sysCodeDO.setExtendsField1("TAX_CATEGORIES_QYSDS");
                sysCodeDO.setExtendsField2("企业所得税");
            } else if (codeName.equals("不限")) {
                // do nothing
            } else if (codeName.startsWith("个人所得税")) {
                sysCodeDO.setCodeName(StringUtils.substringAfter(codeName, "-"));
                sysCodeDO.setExtendsField1("TAX_CATEGORIES_GRSDS");
                sysCodeDO.setExtendsField2("个人所得税");
                sysCodeDO.setCodeStatus(SysCodeStatus.HIDDEN);
            } else {
                throw new RuntimeException("未知的税种类型");
            }
        }
        return sysCodeDOList;
    };

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

    @Accessors(chain = true)
    @Data
    public static class Area {

        private Long id;
        private Long pid;
        private String code;
        private String name;
        private List<Area> cityList;
    }
}

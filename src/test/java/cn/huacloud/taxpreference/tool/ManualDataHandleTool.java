package cn.huacloud.taxpreference.tool;

import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.sync.spider.processor.DocCodeProcessors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.jdbc.Driver;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author wangkh
 */
@Ignore
public class ManualDataHandleTool {

    static JdbcTemplate jdbcTemplate;

    static ObjectMapper objectMapper = new ObjectMapper();

    static String filePath = "D:/preference_data.json";

    /**
     * 数据处理
     */
    @Test
    public void handleData() throws Exception {
        List<PreferenceData> preferenceDataList = readData();
        Map<Long, List<PreferenceData>> preferenceMap = preferenceDataList.stream()
                .filter(preferenceData -> preferenceData.getTaxPreferencePoliciesId() != null)
                .collect(groupingBy(PreferenceData::getTaxPreferenceId));

        List<Preference> preferenceList = new ArrayList<>();
        for (Map.Entry<Long, List<PreferenceData>> entry : preferenceMap.entrySet()) {
            // 税收优惠
            List<PreferenceData> value = entry.getValue();
            PreferenceData preferenceData = value.get(0);
            Preference preference = CustomBeanUtil.copyProperties(preferenceData, Preference.class);
            preference.setPoliciesItems(new ArrayList<>());
            preferenceList.add(preference);

            if (preferenceData.getTaxPreferencePoliciesId() == null) {
                continue;
            }
            Map<Long, List<PreferenceData>> map = value.stream().collect(groupingBy(PreferenceData::getTaxPreferencePoliciesId));

            for (Map.Entry<Long, List<PreferenceData>> subEntry : map.entrySet()) {
                // 税收优惠关联的政策
                List<PreferenceData> subValue = subEntry.getValue();
                PreferenceData subPreferenceData = subValue.get(0);
                PoliciesItem policiesItem = CustomBeanUtil.copyProperties(subPreferenceData, PoliciesItem.class);
                policiesItem.setMatchPolicies(new ArrayList<>());

                preference.getPoliciesItems().add(policiesItem);

                if (subPreferenceData.getPoliciesId() == null) {
                    continue;
                }

                for (PreferenceData data : subValue) {
                    // 匹配的政策
                    Policies policies = CustomBeanUtil.copyProperties(data, Policies.class);
                    policiesItem.getMatchPolicies().add(policies);
                }
            }
        }
        int size = preferenceList.size();
        setFlag(preferenceList);

        Pair<List<Preference>, List<Preference>> pairOne = splitByFlag(preferenceList);

        List<Preference> multiMatchList = pairOne.getSecond();
        // 使用税种匹配 +文号匹配
        for (Preference preference : multiMatchList) {
            String taxCategoriesNames = preference.getTaxCategoriesNames();
            if (StringUtils.isBlank(taxCategoriesNames)) {
                continue;
            }
            for (PoliciesItem policiesItem : preference.getPoliciesItems()) {
                List<Policies> matchPolicies = policiesItem.getMatchPolicies();
                List<Policies> list = matchPolicies.stream().filter(policies -> {
                    String categoriesNames = policies.getCategoriesNames();
                    if (StringUtils.isBlank(categoriesNames)) {
                        return false;
                    }
                    // 税种
                    return taxCategoriesNames.contains(categoriesNames);
                }).collect(toList());
                if (!list.isEmpty()) {

                    String numCode = getNumCode(policiesItem.getManualDocCode());
                    if (list.size() > 1 && numCode != null) {
                        List<Policies> collect = list.stream().filter(policies -> {
                            String docCode = policies.getDocCode();
                            if (docCode == null) {
                                return false;
                            }
                            return docCode.contains(numCode);
                        }).collect(toList());
                        if (!collect.isEmpty()) {
                            policiesItem.setMatchPolicies(collect);
                        }
                    } else {
                        policiesItem.setMatchPolicies(list);
                    }
                }
            }
        }

        setFlag(multiMatchList);

        Pair<List<Preference>, List<Preference>> pairTwo = splitByFlag(multiMatchList);

        // 执行修改
        List<Preference> target = pairOne.getFirst();
        target.addAll(pairTwo.getFirst());

        updatePreference(target);
        System.out.println();
    }

    private void updatePreference(List<Preference> target) {
        for (Preference preference : target) {
            List<PoliciesItem> policiesItems = preference.getPoliciesItems();
            for (PoliciesItem policiesItem : policiesItems) {
                List<Policies> matchPolicies = policiesItem.getMatchPolicies();
                if (matchPolicies.isEmpty()) {
                    throw new RuntimeException("Never happen!");
                }
                Policies policies = matchPolicies.get(0);
                // 设置政策ID
                jdbcTemplate.update("UPDATE t_tax_preference_policies SET policies_id = ? WHERE id = ?", policies.getPoliciesId(), policiesItem.getTaxPreferencePoliciesId());
            }
            // 修改修改税收优惠状态
            jdbcTemplate.update("UPDATE t_tax_preference SET tax_preference_status = 'RELEASED' WHERE id = ?", preference.getTaxPreferenceId());
        }
    }

    private String getNumCode(String docCode) {
        Pattern pattern = Pattern.compile("\\d+号");
        List<DocCodeProcessors.Group> allGroups = DocCodeProcessors.getAllGroups(pattern, docCode);
        if (allGroups.isEmpty()) {
            return null;
        }
        DocCodeProcessors.Group group = allGroups.get(allGroups.size() - 1);
        return group.getGroup();
    }

    private Pair<List<Preference>, List<Preference>> splitByFlag(List<Preference> preferenceList) {
        List<Preference> singleMatch = preferenceList.stream().filter(Preference::getFlag)
                .collect(Collectors.toList());
        List<Preference> multiMatch = preferenceList.stream().filter(preference -> !preference.getFlag())
                .filter(preference -> {
                    List<PoliciesItem> policiesItems = preference.getPoliciesItems();
                    for (PoliciesItem policiesItem : policiesItems) {
                        if (policiesItem.getMatchPolicies().isEmpty()) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
        return Pair.of(singleMatch, multiMatch);
    }

    private void setIdAndRelease(List<Preference> singleMatch) {

    }

    private void setFlag(List<Preference> preferenceList) {
        for (Preference preference : preferenceList) {
            List<PoliciesItem> policiesItems = preference.getPoliciesItems();
            for (PoliciesItem policiesItem : policiesItems) {
                List<Policies> matchPolicies = policiesItem.getMatchPolicies();
                if (matchPolicies.size() == 1) {
                    policiesItem.setFlag(true);
                }
            }
        }
        for (Preference preference : preferenceList) {
            List<PoliciesItem> policiesItems = preference.getPoliciesItems();
            boolean flag = true;
            for (PoliciesItem policiesItem : policiesItems) {
                if (!policiesItem.getFlag()) {
                    flag = false;
                    break;
                }
            }
            preference.setFlag(flag);
        }
    }

    private List<PreferenceData> readData() throws Exception {
        return objectMapper.readValue(new File(filePath), new TypeReference<List<PreferenceData>>() {
        });
    }

    /**
     * 保存数据到本地磁盘
     *
     * @throws Exception
     */
    @Test
    public void saveData() throws Exception {
        String sql =
                "SELECT preference.id                      tax_preference_id,\n" +
                        "       preference.tax_preference_name     tax_preference_name,\n" +
                        "       preference.tax_categories_names    tax_categories_names,\n" +
                        "       preference_policies.id             tax_preference_policies_id,\n" +
                        "       preference_policies.policies_title manual_title,\n" +
                        "       preference_policies.doc_code       manual_doc_code,\n" +
                        "       policies.id                  policies_id,\n" +
                        "       policies.doc_code                  doc_code,\n" +
                        "       policies.tax_categories_names      categories_names\n" +
                        "FROM t_tax_preference preference\n" +
                        "         LEFT JOIN t_tax_preference_policies preference_policies\n" +
                        "                   ON preference.id = preference_policies.tax_preference_id\n" +
                        "         LEFT JOIN t_policies policies\n" +
                        "                   ON preference_policies.policies_title = policies.title\n" +
                        "WHERE preference_policies.policies_id IS NULL";
        List<PreferenceData> preferenceData = jdbcTemplate.query(sql, DataClassRowMapper.newInstance(PreferenceData.class));

        objectMapper.writeValue(new File(filePath), preferenceData);
    }

    @Data
    public static class IdHolder {
        private Long policiesId;
        private Long policiesExplainId;
    }

    @Data
    public static class Preference {
        private Long taxPreferenceId;
        private String taxPreferenceName;
        private String taxCategoriesNames;
        private Boolean flag = false;
        private List<PoliciesItem> policiesItems;
    }

    @Data
    public static class PoliciesItem {
        private Long taxPreferencePoliciesId;
        private String manualTitle;
        private String manualDocCode;
        private Boolean flag = false;
        private List<Policies> matchPolicies;
    }

    @Data
    public static class Policies {
        private Long policiesId;
        private String docCode;
        private String categoriesNames;
    }

    @Data
    public static class PreferenceData {
        private Long taxPreferenceId;
        private String taxPreferenceName;
        private String taxCategoriesNames;
        private Long taxPreferencePoliciesId;
        private String manualTitle;
        private String manualDocCode;
        private Long policiesId;
        private String docCode;
        private String categoriesNames;
    }

    @Test
    public void deleteData() throws Exception {
        List<String> spiderIds = FileUtils.readLines(new File("D:/爬虫库删除的政策.csv"), StandardCharsets.UTF_8)
                .stream().filter(StringUtils::isNotBlank)
                .collect(toList());
        for (String spiderId : spiderIds) {
            // deleteData(spiderId);
        }
        System.out.println();
    }

    private void deleteData(String spiderId) {
        // 政策解读
        String sql = "SELECT t_spider_data_sync.doc_id policies_id,\n" +
                "       t_policies_explain.id policies_explain_id\n" +
                "FROM t_spider_data_sync\n" +
                "         LEFT JOIN t_policies_explain\n" +
                "                   ON t_policies_explain.policies_id = t_spider_data_sync.doc_id\n" +
                "WHERE t_spider_data_sync.spider_data_id = ?\n" +
                "  AND t_spider_data_sync.doc_type = 'POLICIES'";
        List<IdHolder> idHolderList = jdbcTemplate.query(sql, DataClassRowMapper.newInstance(IdHolder.class), spiderId);

        for (IdHolder idHolder : idHolderList) {
            Long policiesExplainId = idHolder.getPoliciesExplainId();
            if (policiesExplainId != null) {
                // 删除政策解读
                int update = jdbcTemplate.update("DELETE FROM t_policies_explain WHERE id = ?", policiesExplainId);
                // 删除同步记录
                int update1 = jdbcTemplate.update("DELETE FROM t_spider_data_sync WHERE doc_id = ? AND doc_type = 'POLICIES_EXPLAIN'", policiesExplainId);
            }
            Long policiesId = idHolder.getPoliciesId();
            if (policiesId != null) {
                // 删除政策
                int update = jdbcTemplate.update("DELETE FROM t_policies WHERE id = ?", policiesId);
                // 删除同步记录
                int update1 = jdbcTemplate.update("DELETE FROM t_spider_data_sync WHERE doc_id = ? AND doc_type = 'POLICIES'", policiesId);
            }
        }
        System.out.println();
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        DataSourceProperties properties = new DataSourceProperties();
        properties.setDriverClassName(Driver.class.getName());
        properties.setUrl("jdbc:mysql://1.117.164.184:45210/tax_preference?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        properties.setUsername("");
        properties.setPassword("");
        jdbcTemplate = new JdbcTemplate(properties.initializeDataSourceBuilder().build(), true);
    }
}

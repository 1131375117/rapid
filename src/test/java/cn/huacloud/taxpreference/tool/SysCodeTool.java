package cn.huacloud.taxpreference.tool;

import cn.huacloud.taxpreference.BaseApplicationTest;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统码值工具类
 *
 * @author wangkh
 */
@Slf4j
public class SysCodeTool /*extends BaseApplicationTest*/ {

    @Test
    public void generateSysCode() {
        List<SysCodeDO> allSysCodes = Arrays.stream(SysCodeTool.class.getDeclaredFields())
                .filter(field -> field.getType().isAssignableFrom(SysCodeProvider.class))
                .map(field -> {
                    try {
                        return (SysCodeProvider) field.get(this);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull)
                .flatMap(sysCodeProvider -> sysCodeProvider.fetch().stream())
                .collect(Collectors.toList());

        // 设置ID和sort
        for (int i = 0; i < allSysCodes.size(); i++) {
            SysCodeDO sysCodeDO = allSysCodes.get(i);
            sysCodeDO.setId((long) (i + 1));
            sysCodeDO.setSort(sysCodeDO.getId());
        }

        log.info("系统码值生成完毕");
    }

    /**
     * 系统码值提供器
     */
    interface SysCodeProvider {
        List<SysCodeDO> fetch();
    }

    /**
     * 所属税种
     */
    private SysCodeProvider taxCategories = () -> {

        return new ArrayList<>();
    };

    /**
     * 所属区域
     */
    private SysCodeProvider area = () -> {
        return new ArrayList<>();
    };

    /**
     * 适用行业
     */
    private SysCodeProvider industry = () -> {
        return new ArrayList<>();
    };

    /**
     * 纳税人资格认定类型
     */
    private SysCodeProvider taxpayerIdentifyType = () -> {
        return new ArrayList<>();
    };

    /**
     * 适用企业类型
     */
    private SysCodeProvider enterpriseType = () -> {
        return new ArrayList<>();
    };

    /**
     * 纳税人登记注册类型
     */
    private SysCodeProvider taxpayerRegisterType = () -> {
        return new ArrayList<>();
    };

    /**
     * 纳税人类型
     */
    private SysCodeProvider taxpayerType = () -> {
        return new ArrayList<>();
    };


}

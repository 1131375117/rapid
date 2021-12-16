package cn.huacloud.taxpreference.services.common.handler.param;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.common.utils.CustomStringUtil;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.ConditionQueryDTO;
import cn.huacloud.taxpreference.services.common.entity.vos.ConditionGroupVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 税收优惠条件处理参数处理器
 *
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TaxPreferenceConditionParamHandler implements SysParamHandler<ConditionQueryDTO, ConditionGroupVO> {

    private final SysCodeService sysCodeService;

    @Override
    public ConditionGroupVO handle(List<SysParamDO> sysParams, ConditionQueryDTO handlerParam) {
        List<String> taxCategoriesCodes = handlerParam.getTaxCategoriesCodes();
        List<String> exemptMatterCodes = handlerParam.getExemptMatterCodes();

        // 合并过滤条件
        Set<String> taxCategoriesSet = new HashSet<>(taxCategoriesCodes);
        Set<String> personConditionSet = new HashSet<>();
        exemptMatterCodes.stream()
                .map(codeValue -> sysCodeService.getSysCodeDO(SysCodeType.EXEMPT_MATTER, codeValue))
                .filter(Objects::nonNull)
                .forEach(sysCodeDO -> {
                    String extendsField1 = sysCodeDO.getExtendsField1();
                    if (StringUtils.isNotBlank(extendsField1)) {
                        taxCategoriesSet.add(extendsField1);
                    }
                    String extendsField2 = sysCodeDO.getExtendsField2();
                    if (StringUtils.isNotBlank(extendsField2)) {
                        personConditionSet.add(extendsField2);
                    }
                });
        // 数据过滤
        List<SysParamWrapper> sysParamWrapperList = sysParams.stream()
                .map(sysParamDO -> {
                    Set<String> set = CustomStringUtil.spiltStringToSet(sysParamDO.getExtendsField1());
                    if (set.isEmpty()) {
                        log.error("系统参数配置不正确，ID：{}", sysParamDO.getId());
                    }
                    return new SysParamWrapper(sysParamDO, set);
                })
                .filter(wrapper -> {
                    boolean disjoint = Collections.disjoint(wrapper.getTaxCategoriesCodes(), taxCategoriesSet);
                    if (disjoint) {
                        return false;
                    }
                    SysParamDO sysParamDO = wrapper.getSysParamDO();
                    String extendsField3 = sysParamDO.getExtendsField3();
                    if (StringUtils.isNotBlank(extendsField3)) {
                        return personConditionSet.contains(extendsField3);
                    }
                    return true;
                }).collect(toList());
        return null;
    }

    @AllArgsConstructor
    @Data
    static class SysParamWrapper {
        private SysParamDO sysParamDO;
        private Set<String> taxCategoriesCodes;
    }
}
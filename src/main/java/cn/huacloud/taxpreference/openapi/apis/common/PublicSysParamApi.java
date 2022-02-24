package cn.huacloud.taxpreference.openapi.apis.common;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.entity.vos.ChoiceGroupVO;
import cn.huacloud.taxpreference.common.entity.vos.GroupVO;
import cn.huacloud.taxpreference.common.utils.CustomStringUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
@Api(tags = "公开的系统参数")
@RequiredArgsConstructor
@RequestMapping("/open-api/v1")
@RestController
public class PublicSysParamApi {

    private final SysParamService sysParamService;

    @ApiOperation("政策所属专题列表")
    @OpenApiCheckToken
    @GetMapping("/sys/param/policies/specialSubjects")
    public ResultVO<List<String>> getPoliciesSpecialSubjects() {
        List<String> list = sysParamService.getMapParamByTypes(String.class, SysParamTypes.POLICIES_SPECIAL_SUBJECT)
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return ResultVO.ok(list);
    }

    @ApiOperation("税收优惠和税种绑定的自定义条件")
    @OpenApiCheckToken
    @PostMapping("/sys/param/BasePreferenceCondition")
    public ResultVO<List<ChoiceGroupVO<String>>> getBasePreferenceCondition(@RequestBody Set<String> taxCategoriesCode) {
        List<ChoiceGroupVO<String>> conditions = sysParamService.getSysParamDOByTypes(SysParamTypes.TAX_PREFERENCE_CONDITION).stream()
                .filter(sysParamDO -> !"自定义条件".equals(sysParamDO.getExtendsField3()))
                .filter(sysParamDO -> CustomStringUtil.haveIntersection(sysParamDO.getExtendsField1(), taxCategoriesCode))
                .map(sysParamDO -> {
                    ChoiceGroupVO<String> condition = new ChoiceGroupVO<>();
                    condition.setMultipleChoice("多选".equals(sysParamDO.getExtendsField5()))
                            .setName(sysParamDO.getParamName())
                            .setValues(CustomStringUtil.arrayStringToList(sysParamDO.getParamValue()));
                    return condition;
                })
                .collect(Collectors.toList());
        return ResultVO.ok(conditions);
    }

    @ApiOperation("税收优惠自定义条件")
    @OpenApiCheckToken
    @GetMapping("/sys/param/customPreferenceCondition")
    public ResultVO<List<GroupVO<ChoiceGroupVO<String>>>> getCustomPreferenceCondition() {
        List<GroupVO<ChoiceGroupVO<String>>> result = sysParamService.getSysParamDOByTypes(SysParamTypes.TAX_PREFERENCE_CONDITION).stream()
                .filter(sysParamDO -> "自定义条件".equals(sysParamDO.getExtendsField3()))
                .sorted(Comparator.comparing(SysParamDO::getParamKey))
                .collect(Collectors.groupingBy(SysParamDO::getExtendsField2, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> {
                    List<ChoiceGroupVO<String>> conditions = entry.getValue().stream()
                            .map(sysParamDO -> {
                                ChoiceGroupVO<String> condition = new ChoiceGroupVO<>();
                                condition.setMultipleChoice("多选".equals(sysParamDO.getExtendsField5()))
                                        .setName(sysParamDO.getParamName())
                                        .setValues(CustomStringUtil.arrayStringToList(sysParamDO.getParamValue()));
                                return condition;
                            })
                            .collect(Collectors.toList());
                    return new GroupVO<ChoiceGroupVO<String>>().setName(entry.getKey()).setValues(conditions);
                }).collect(Collectors.toList());
        return ResultVO.ok(result);
    }

    @ApiOperation("税务实务数据列表")
    @OpenApiCheckToken
    @GetMapping("/sys/param/consultationTypes")
    public ResultVO<List<String>> getConsultationTypes() {
        List<String> list = sysParamService.getMapParamByTypes(String.class, SysParamTypes.TAX_CONSULTATION)
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return ResultVO.ok(list);
    }

}

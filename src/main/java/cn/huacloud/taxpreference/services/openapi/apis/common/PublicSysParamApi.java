package cn.huacloud.taxpreference.services.openapi.apis.common;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.entity.vos.ChoiceGroupVO;
import cn.huacloud.taxpreference.common.entity.vos.GroupVO;
import cn.huacloud.taxpreference.common.utils.CustomStringUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
@ApiSupport(order = 200)
@Api(tags = "公开系统参数")
@RequiredArgsConstructor
@RequestMapping("/open-api/v1")
@RestController
public class PublicSysParamApi {

    private final SysParamService sysParamService;

    @ApiOperationSupport(order = 1)
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

    @ApiOperationSupport(order = 2)
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

    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "税种相关的税收优惠基础条件", notes = "税收优惠高级搜索页，通过选择税种，会联动展示基础条件，传入税种即可获取与次税种相关的基础条件。示例参数：[10101,10104]")
    @OpenApiCheckToken
    @PostMapping("/sys/param/basePreferenceCondition")
    public ResultVO<List<ChoiceGroupVO<String>>> getBasePreferenceCondition(@RequestBody Set<String> taxCategoriesCodes) {
        List<ChoiceGroupVO<String>> conditions = sysParamService.getSysParamDOByTypes(SysParamTypes.TAX_PREFERENCE_CONDITION).stream()
                .filter(sysParamDO -> !"自定义条件".equals(sysParamDO.getExtendsField3()))
                .filter(sysParamDO -> CustomStringUtil.haveIntersection(sysParamDO.getExtendsField1(), taxCategoriesCodes))
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

    /*@ApiOperationSupport(order = 4)
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
    }*/

}

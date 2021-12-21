package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.entity.vos.GroupVO;
import cn.huacloud.taxpreference.common.utils.CustomStringUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
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
@RequestMapping("/api/v1")
@RestController
public class PublicSysParamController {

    private final SysParamService sysParamService;

    @ApiOperation("政策所属专题列表")
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
    @PostMapping("/sys/param/BasePreferenceCondition")
    public ResultVO<List<GroupVO<String>>> getBasePreferenceCondition(@RequestBody Set<String> taxCategoriesCode) {
        List<GroupVO<String>> conditions = sysParamService.getSysParamDOByTypes(SysParamTypes.TAX_PREFERENCE_CONDITION).stream()
                .filter(sysParamDO -> !"自定义条件".equals(sysParamDO.getExtendsField3()))
                .filter(sysParamDO -> CustomStringUtil.haveIntersection(sysParamDO.getExtendsField1(), taxCategoriesCode))
                .map(sysParamDO -> new GroupVO<String>().setName(sysParamDO.getParamName())
                        .setValues(CustomStringUtil.arrayStringToList(sysParamDO.getParamValue())))
                .collect(Collectors.toList());
        return ResultVO.ok(conditions);
    }

    @ApiOperation("税收优惠自定义条件")
    @GetMapping("/sys/param/customPreferenceCondition")
    public ResultVO<List<GroupVO<GroupVO<String>>>> getCustomPreferenceCondition() {
        List<GroupVO<GroupVO<String>>> result = sysParamService.getSysParamDOByTypes(SysParamTypes.TAX_PREFERENCE_CONDITION).stream()
                .filter(sysParamDO -> "自定义条件".equals(sysParamDO.getExtendsField3()))
                .sorted(Comparator.comparing(SysParamDO::getParamKey))
                .collect(Collectors.groupingBy(SysParamDO::getExtendsField2, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> {
                    List<GroupVO<String>> conditions = entry.getValue().stream()
                            .map(sysParamDO -> new GroupVO<String>().setName(sysParamDO.getParamName())
                                    .setValues(CustomStringUtil.arrayStringToList(sysParamDO.getParamValue())))
                            .collect(Collectors.toList());
                    return new GroupVO<GroupVO<String>>().setName(entry.getKey()).setValues(conditions);
                }).collect(Collectors.toList());
        return ResultVO.ok(result);
    }

}

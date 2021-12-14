package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.SysParamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
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

    @ApiOperation("获取政策所属专题")
    @GetMapping("/sys/param/policies/specialSubjects")
    public ResultVO<List<String>> getPoliciesSpecialSubjects() {
        List<String> list = sysParamService.getMapParamByTypes(String.class, SysParamTypes.POLICIES_SPECIAL_SUBJECT)
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return ResultVO.ok(list);
    }
}

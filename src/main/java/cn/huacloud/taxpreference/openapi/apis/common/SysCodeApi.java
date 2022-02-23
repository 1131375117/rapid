package cn.huacloud.taxpreference.openapi.apis.common;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Api("系统码值")
@RequestMapping("/open-api/v1")
@RestController
public class SysCodeApi {
    private final SysCodeService sysCodeService;

    @OpenApiCheckToken
    @ApiOperation("获取所有码值类型")
    @GetMapping("/sys/codeTypes")
    public ResultVO<List<SysCodeSimpleVO>> getSysCodeTypes() {
        List<SysCodeSimpleVO> list = Arrays.stream(SysCodeType.values())
                .map(sysCodeType -> new SysCodeSimpleVO()
                        .setCodeName(sysCodeType.name)
                        .setCodeValue(sysCodeType.getValue()))
                .collect(Collectors.toList());
        return ResultVO.ok(list);
    }

    @OpenApiCheckToken
    @ApiOperation(value = "获取系统码值", notes = "参数 sysCodeType 是提供码值类型")
    @GetMapping("/sys/codes")
    public ResultVO<List<SysCodeTreeVO>> getSysCodes(@RequestParam("sysCodeType") SysCodeType sysCodeType) {
        List<SysCodeTreeVO> sysCodeVOList = sysCodeService.getSysCodeTreeVO(sysCodeType);
        return ResultVO.ok(sysCodeVOList);
    }

    @OpenApiCheckToken
    @ApiOperation(value = "获取税收优惠的系统码值", notes = "参数 sysCodeType 是提供码值类型")
    @GetMapping("/sys/codes/taxPreference")
    public ResultVO<List<SysCodeTreeVO>> getTaxPreferenceSysCodes(@RequestParam("sysCodeType") SysCodeType sysCodeType) {
        List<SysCodeTreeVO> sysCodeVOList;
        // 税收优惠的税种范围不一样
        if (SysCodeType.TAX_CATEGORIES == sysCodeType) {
            sysCodeVOList = sysCodeService.getSysCodeTreeVO(sysCodeType,
                    sysCodeDO -> "true".equals(sysCodeDO.getExtendsField1()));
        } else {
            sysCodeVOList = sysCodeService.getSysCodeTreeVO(sysCodeType);
        }
        return ResultVO.ok(sysCodeVOList);
    }
}

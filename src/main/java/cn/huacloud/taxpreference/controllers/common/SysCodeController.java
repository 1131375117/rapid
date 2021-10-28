package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统码值接口
 * @author wangkh
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class SysCodeController {

    private final SysCodeService sysCodeService;

    @ApiOperation(value = "获取系统码值", notes = "参数 sysCodeType 是提供码值类型")
    @GetMapping("/sys/codes")
    public ResultVO<List<SysCodeTreeVO>> getSysCodes(@RequestParam("sysCodeType") SysCodeType sysCodeType) {
        List<SysCodeTreeVO> sysCodeVOList = sysCodeService.getSysCodeTreeVO(sysCodeType);
        return ResultVO.ok(sysCodeVOList);
    }
}

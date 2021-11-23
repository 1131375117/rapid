package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.config.SysConfig;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangkh
 */
@Api(tags = "系统工具接口")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class SysToolController {

    private final SysConfig sysConfig;

    private final SysCodeService sysCodeService;

    @ApiOperation("清除系统码值缓存")
    @PostMapping("/tool/cleanSysCodeCache")
    public ResultVO<Void> cleanSysCodeCache(String password) {
        sysConfig.checkSysPassword(password);
        sysCodeService.cleanSysCodeCache();
        return ResultVO.ok();
    }

}

package cn.huacloud.taxpreference.controllers.tool;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.config.SysConfig;
import cn.huacloud.taxpreference.services.common.CacheClear;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangkh
 */
@Api(tags = "系统工具接口")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Slf4j
public class SysToolController {

    private final SysConfig sysConfig;

    private final List<CacheClear> cacheClears;

    @ApiOperation("清除系统缓存")
    @PostMapping("/tool/cleanSysCodeCache")
    public ResultVO<Void> cleanSysCodeCache(String password) {
        sysConfig.checkSysPassword(password);
        for (CacheClear cacheClear : cacheClears) {
            try {
                cacheClear.clear();
            } catch (Exception e) {
                log.error("缓存清除失败{}:", cacheClear.getClass().getName(), e);
            }
        }
        return ResultVO.ok();
    }

}

package cn.huacloud.taxpreference.controllers.tool;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.config.SysConfig;
import cn.huacloud.taxpreference.services.common.CacheClear;
import cn.huacloud.taxpreference.services.wework.WeWorkTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.huacloud.taxpreference.services.wework.support.WeWorkConstants.APP_NAME_TAX_PREFERENCE;

/**
 * @author wangkh
 */
@Api(tags = "系统工具接口")
@RequiredArgsConstructor
@RestController
@Slf4j
public class SysToolController {

    private final SysConfig sysConfig;

    private final List<CacheClear> cacheClears;

    private final WeWorkTokenService weWorkTokenService;

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

    @ApiOperation("获取第三方应用凭证")
    @GetMapping("/tool/getWeWorkSuitToken")
    public ResultVO<String> getWeWorkSuitToken() {
        String suiteToken = weWorkTokenService.getSuiteToken(APP_NAME_TAX_PREFERENCE);
        return ResultVO.ok(suiteToken);
    }
}

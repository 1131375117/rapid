package cn.huacloud.taxpreference.controllers.tool;

import cn.huacloud.taxpreference.common.utils.PasswordSecureUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.config.SysConfig;
import cn.huacloud.taxpreference.services.common.CacheClear;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
public class SysToolController {

    private final SysConfig sysConfig;

    private final List<CacheClear> cacheClears;

    @ApiOperation("清除系统缓存")
    @PostMapping("/tool/cleanSysCodeCache")
    public ResultVO<Void> cleanSysCodeCache(String password) {
        sysConfig.checkSysPassword(password);
        for (CacheClear cacheClear : cacheClears) {
            cacheClear.clear();
        }
        return ResultVO.ok();
    }

    @ApiOperation("把命名密码加密成密文")
    @PostMapping("/tool/encryptPassword")
    public ResultVO<String> getEncryptPassword(String value) {
        return ResultVO.ok(PasswordSecureUtil.encrypt(value));
    }
}

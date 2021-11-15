package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.config.SysConfig;
import cn.huacloud.taxpreference.sync.es.trigger.impl.PoliciesEventTrigger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangkh
 */
@Api("数据同步前端控制器")
@AllArgsConstructor
@RestController
public class DataSyncController {

    private SysConfig sysConfig;

    private PoliciesEventTrigger policiesEventTrigger;

    @ApiOperation("同步所有政策法规数据")
    @GetMapping("/sync/policies")
    public ResultVO<Void> syncAllPolicies(String password) {
        checkSysPassword(password);
        policiesEventTrigger.syncAll();
        return ResultVO.ok();
    }

    /**
     * 检查系统密码
     * @param password 系统密码
     */
    private void checkSysPassword(String password) {
        if (!sysConfig.getPassword().equals(password)) {
            throw BizCode._4402.exception();
        }
    }
}

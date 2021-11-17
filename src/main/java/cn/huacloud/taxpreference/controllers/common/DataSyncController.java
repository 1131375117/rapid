package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.config.SysConfig;
import cn.huacloud.taxpreference.sync.es.trigger.impl.PoliciesEventTrigger;
import cn.huacloud.taxpreference.sync.es.trigger.impl.PoliciesExplainEventTrigger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangkh
 */
@Api(tags = "数据同步前端控制器")
@AllArgsConstructor
@RestController
public class DataSyncController {

    private SysConfig sysConfig;

    private PoliciesEventTrigger policiesEventTrigger;

    private PoliciesExplainEventTrigger policiesExplainEventTrigger;

    @ApiOperation("同步所有政策法规数据")
    @GetMapping("/sync/policies")
    public ResultVO<Void> syncAllPolicies(String password) {
        checkSysPassword(password);
        long total = policiesEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
    }

    @ApiOperation("同步所有政策解读数据")
    @GetMapping("/sync/policiesExplain")
    public ResultVO<Void> syncAllPoliciesExplain(String password) {
        checkSysPassword(password);
        long total = policiesExplainEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
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

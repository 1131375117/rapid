package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.config.SysConfig;
import cn.huacloud.taxpreference.sync.es.trigger.impl.FAQEventTrigger;
import cn.huacloud.taxpreference.sync.es.trigger.impl.PoliciesEventTrigger;
import cn.huacloud.taxpreference.sync.es.trigger.impl.PoliciesExplainEventTrigger;
import cn.huacloud.taxpreference.sync.es.trigger.impl.TaxPreferenceEventTrigger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangkh
 */
@Api(tags = "数据同步前端控制器")
@RequiredArgsConstructor
@RestController
public class DataSyncController {

    private final SysConfig sysConfig;

    private final PoliciesEventTrigger policiesEventTrigger;

    private final PoliciesExplainEventTrigger policiesExplainEventTrigger;

    private final FAQEventTrigger faqEventTrigger;

    private final TaxPreferenceEventTrigger taxPreferenceEventTrigger;

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

    @ApiOperation("同步所有热点问答数据")
    @GetMapping("/sync/faq")
    public ResultVO<Void> syncAllFAQ(String password) {
        checkSysPassword(password);
        long total = faqEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
    }

    @ApiOperation("同步所有税收优惠数据")
    @GetMapping("/sync/faq")
    public ResultVO<Void> syncAllTaxPreference(String password) {
        checkSysPassword(password);
        long total = taxPreferenceEventTrigger.syncAll();
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

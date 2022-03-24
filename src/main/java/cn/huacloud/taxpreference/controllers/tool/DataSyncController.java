package cn.huacloud.taxpreference.controllers.tool;

import cn.huacloud.taxpreference.common.enums.JobType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.config.SysConfig;
import cn.huacloud.taxpreference.sync.es.trigger.impl.*;
import cn.huacloud.taxpreference.sync.spider.DataSyncJobParam;
import cn.huacloud.taxpreference.sync.spider.SpiderDataSyncScheduler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangkh
 */
@Api(tags = "数据同步")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class DataSyncController {

    private final SysConfig sysConfig;

    private final PoliciesEventTrigger policiesEventTrigger;

    private final PoliciesExplainEventTrigger policiesExplainEventTrigger;

    private final FAQEventTrigger faqEventTrigger;

    private final TaxPreferenceEventTrigger taxPreferenceEventTrigger;

    private final ConsultationEventTrigger consultationEventTrigger;

    private final OtherDocEventTrigger otherDocEventTrigger;

    private final SpiderDataSyncScheduler spiderDataSyncScheduler;

    @ApiOperation("同步所有政策法规数据")
    @GetMapping("/sync/policies")
    public ResultVO<Void> syncAllPolicies(String password) {
        sysConfig.checkSysPassword(password);
        long total = policiesEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
    }

    @ApiOperation("同步所有政策解读数据")
    @GetMapping("/sync/policiesExplain")
    public ResultVO<Void> syncAllPoliciesExplain(String password) {
        sysConfig.checkSysPassword(password);
        long total = policiesExplainEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
    }

    @ApiOperation("同步所有热点问答数据")
    @GetMapping("/sync/faq")
    public ResultVO<Void> syncAllFAQ(String password) {
        sysConfig.checkSysPassword(password);
        long total = faqEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
    }

    @ApiOperation("同步所有税收优惠数据")
    @GetMapping("/sync/taxPreference")
    public ResultVO<Void> syncAllTaxPreference(String password) {
        sysConfig.checkSysPassword(password);
        long total = taxPreferenceEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
    }

    @ApiOperation("同步所有案例数据")
    @GetMapping("/sync/otherDoc")
    public ResultVO<Void> syncAllOtherDoc(String password) {
        sysConfig.checkSysPassword(password);
        long total = otherDocEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
    }

    @ApiOperation("同步爬虫数据")
    @PostMapping("/sync/spiderData")
    public ResultVO<Void> syncSpiderData(@RequestBody JobParam jobParam) {
        sysConfig.checkSysPassword(jobParam.getPassword());
        spiderDataSyncScheduler.executeJobs(jobParam,JobType.INSERT);
        return ResultVO.ok();
    }

    @ApiOperation("更新已发布的爬虫数据")
    @PostMapping("/sync/refreshSpiderData")
    public ResultVO<Void> refreshSpiderData(@RequestBody JobParam jobParam) {
        sysConfig.checkSysPassword(jobParam.getPassword());
        spiderDataSyncScheduler.executeJobs(jobParam,JobType.UPDATE);
        return ResultVO.ok();
    }

    @ApiOperation("同步热门咨询数据")
    @GetMapping("/sync/consultation")
    public ResultVO<Void> syncAllTaxConsultation(String password) {
        sysConfig.checkSysPassword(password);
        long total = consultationEventTrigger.syncAll();
        return ResultVO.ok().setMsg(MessageFormatter.format("成功同步数据{}条", total).getMessage());
    }

    @Getter
    @Setter
    public static class JobParam extends DataSyncJobParam {
        @ApiModelProperty(example = "12345678Aa")
        private String password;
    }
}

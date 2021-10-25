package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.ProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/**
 * 流程管理接口
 * @author fuhua
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Api(tags = "流程管理接口")
public class ProcessController {
    private final ProcessService processService;
    /**
     * 税收优惠事项审核列表
     * 参数(可选):优惠事项名称
     */

    /**
     * 税收优惠事项申请详情展示
     * 查询条件:优惠事项id
     */


    /**
     * 税收优惠事项批量发布申请
     * 发布申请条件:优惠事项idlist
     */
    @ApiOperation("税收优惠事项批量发布申请")
    @PostMapping("/process/{taxPreferenceId}")
    public ResultVO<Void> insertTaxPreference(@Validated()@NotEmpty(message = "税收优惠id不能为空") @PathVariable("taxPreferenceId") Long taxPreferenceId) {
        return processService.insertProcessService(taxPreferenceId,UserUtil.getCurrentUser());
    }

    /**
     * 税收优惠事项审核提交
     *  审核必传参数:税收优惠事项id,审核结果
     *        可选:备注审核信息(审核结果不通过时为必填)
     */

}

package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.process.ProcessStatus;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.ProcessService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessSubmitDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
    @ApiOperation("税收优惠事项批量发布申请")
    @PostMapping("/queryProcessList")
    public ResultVO<PageVO<ProcessListVO>>queryProcessList(ProcessListDTO processListDTO) {
        return processService.queryProcessList(processListDTO);
    }

    /**
     * 税收优惠事项批量发布申请
     * 发布申请条件:优惠事项idlist
     */
    @ApiOperation("税收优惠事项批量发布申请")
    @PostMapping("/process/{taxPreferenceId}")
    public ResultVO<Void> insertTaxPreference(@Validated()@NotEmpty(message = "税收优惠id不能为空") @PathVariable("taxPreferenceId") Long[] taxPreferenceIds) {
        return processService.insertProcessService(taxPreferenceIds,UserUtil.getCurrentUser());
    }

    /**
     * 税收优惠事项审核提交
     *  审核必传参数:税收优惠事项id,审核结果
     *        可选:备注审核信息(审核结果不通过时为必填)
     */
    @ApiOperation("税收优惠事项审核提交")
    @PostMapping("/submitProcess")
    public ResultVO<Void> insertTaxPreference(@Validated() ProcessSubmitDTO processSubmitDTO)  {
        if(ProcessStatus.RETURNED.name().equals(processSubmitDTO.getTaxPreferenceStatus())&& StringUtils.isBlank(processSubmitDTO.getApprovalNote())){
            throw BizCode._4301.exception();
        }
        return processService.submitProcess(processSubmitDTO,UserUtil.getCurrentUser());
    }


}

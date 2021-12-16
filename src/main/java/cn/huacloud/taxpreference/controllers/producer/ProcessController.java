package cn.huacloud.taxpreference.controllers.producer;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.enums.process.ProcessStatus;
import cn.huacloud.taxpreference.common.utils.ProducerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.ProcessService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessSubmitDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessInfoVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 流程管理接口
 *
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
	@PermissionInfo(name = "税收优惠事项审核列表", group = PermissionGroup.APPROVAL)
	@SaCheckPermission("producer_process_query")
	@ApiOperation("税收优惠事项审核列表")
	@PostMapping("/queryProcessList")
	public ResultVO<PageVO<ProcessListVO>> queryProcessList(@RequestBody ProcessListDTO processListDTO) {
		return processService.queryProcessList(processListDTO, ProducerUserUtil.getCurrentUser().getId());
	}

	/**
	 * 税收优惠事项批量发布申请
	 * 发布申请条件:优惠事项idlist
	 */
	@PermissionInfo(name = "税收优惠事项发布", group = PermissionGroup.APPROVAL)
	@SaCheckPermission("producer_process_release")
	@ApiOperation("税收优惠事项批量发布申请")
	@PostMapping("/process/{taxPreferenceId}")
	public ResultVO<Void> insertTaxPreference(@Validated() @NotEmpty(message = "税收优惠id不能为空") @PathVariable("taxPreferenceId") Long[] taxPreferenceIds) throws MethodArgumentNotValidException {
		return processService.insertProcessService(taxPreferenceIds, ProducerUserUtil.getCurrentUser());

	}

	/**
	 * 税收优惠事项审核提交
	 * 审核必传参数:税收优惠事项id,审核结果
	 * 可选:备注审核信息(审核结果不通过时为必填)
	 */
	@PermissionInfo(name = "税收优惠事项审批", group = PermissionGroup.APPROVAL)
	@SaCheckPermission("producer_process_submit")
	@ApiOperation("税收优惠事项审批")
	@PostMapping("/submitProcess")
	public ResultVO<Void> insertTaxPreference(@RequestBody @Validated() ProcessSubmitDTO processSubmitDTO) {
		if (ProcessStatus.RETURNED.equals(processSubmitDTO.getTaxPreferenceStatus())
				&& StringUtils.isBlank(processSubmitDTO.getApprovalNote())) {
			throw BizCode._4301.exception();
		}
		return processService.submitProcess(processSubmitDTO, ProducerUserUtil.getCurrentUser());
	}

	/**
	 * 流程审批信息
	 */
	@PermissionInfo(name = "税收优惠事项流程审批详情", group = PermissionGroup.APPROVAL)
	@SaCheckPermission("producer_process_approval_detail")
	@ApiOperation("税收优惠事项审批信息")
	@PostMapping("/queryProcessInfo/{id}")
	public ResultVO<List<ProcessInfoVO>> queryProcessInfo(@Validated() @NotEmpty(message = "税收优惠id不能为空") @PathVariable Long id) {
		return processService.queryProcessInfo(id);
	}


}

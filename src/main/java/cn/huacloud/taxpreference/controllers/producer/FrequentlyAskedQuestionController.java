package cn.huacloud.taxpreference.controllers.producer;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.FrequentlyAskedQuestionDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.FrequentlyAskedQuestionVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 热门问答接口
 *
 * @author wuxin
 */
@Api(tags = "热门问答")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class FrequentlyAskedQuestionController {

	private final FrequentlyAskedQuestionService frequentlyAskedQuestionService;

	@PermissionInfo(name = "热门问答分页列表", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
	@SaCheckPermission("producer_frequentlyAskedQuestion_query")
	@ApiOperation("热门问答分页列表")
	@PostMapping(value = "/getFrequentlyAskedQuestionList/query")
	public ResultVO<PageVO<FrequentlyAskedQuestionVO>> getFrequentlyAskedQuestionList(
			@RequestBody QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
		queryPoliciesExplainDTO.paramReasonable();
		PageVO<FrequentlyAskedQuestionVO> frequentlyAskedQuestionPageVO =
				frequentlyAskedQuestionService.getFrequentlyAskedQuestionList(queryPoliciesExplainDTO);
		return ResultVO.ok(frequentlyAskedQuestionPageVO);
	}

	/**
	 * 新增热门问答
	 *
	 * @param frequentlyAskedQuestionDTO
	 * @return
	 */
	@PermissionInfo(name = "热门问答新增", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
	@SaCheckPermission("producer_frequentlyAskedQuestion_insert")
	@ApiOperation("热门问答新增")
	@PostMapping(value = "/frequentlyAskedQuestion")
	public ResultVO<Void> insertFrequentlyAskedQuestion(
			@Validated(ValidationGroup.Create.class) @RequestBody
					FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
		frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(
				frequentlyAskedQuestionDTO, UserUtil.getCurrentUser().getId());
		return ResultVO.ok();
	}

	/**
	 * 修改热门问答
	 *
	 * @param frequentlyAskedQuestionDto
	 * @return
	 */
	@PermissionInfo(name = "热门问答修改", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
	@SaCheckPermission("producer_frequentlyAskedQuestion_update")
	@ApiOperation("热门问答修改")
	@PutMapping(value = "/frequentlyAskedQuestion")
	public ResultVO<Void> updateFrequentlyAskedQuestion(
			@Validated(ValidationGroup.Update.class) @RequestBody
					FrequentlyAskedQuestionDTO frequentlyAskedQuestionDto) {
		frequentlyAskedQuestionService.updateFrequentlyAskedQuestion(frequentlyAskedQuestionDto);
		return ResultVO.ok();
	}

	@PermissionInfo(name = "热门问答详情", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
	@SaCheckPermission("producer_frequentlyAskedQuestion_detail")
	@ApiOperation("热门问答详情")
	@GetMapping(value = "/frequentlyAskedQuestion/{id}")
	public ResultVO<FrequentlyAskedQuestionDetailVO> getFrequentlyAskedQuestionById(
			@Validated() @PathVariable("id") Long id) {
		FrequentlyAskedQuestionDetailVO frequentlyAskedQuestionDetail =
				frequentlyAskedQuestionService.getFrequentlyAskedQuestionById(id);
		return ResultVO.ok(frequentlyAskedQuestionDetail);
	}

	@PermissionInfo(name = "热门问答删除", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
	@SaCheckPermission("producer_frequentlyAskedQuestion_delete")
	@ApiOperation("热门问答删除")
	@DeleteMapping(value = "/frequentlyAskedQuestion/{id}")
	public ResultVO<Void> deleteFrequentlyAskedQuestion(
			@Validated @NotEmpty(message = "id不能为空") @NotEmpty(message = "id不能为空") @PathVariable("id")
					Long id) {
		frequentlyAskedQuestionService.deleteFrequentlyAskedQuestion(id);
		return ResultVO.ok();
	}
}

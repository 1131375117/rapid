package cn.huacloud.taxpreference.controllers.producer;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ProducerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesCombinationDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryAbolishDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesCheckDeleteVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

/**
 * 政策法规接口
 *
 * @author wuxin
 */
@Slf4j
@Api(tags = "政策法规")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PoliciesController {

	private final PoliciesService policiesService;

	/**
	 * 政策列表查询接口
	 *
	 * @param queryPoliciesDTO 政策法规查询条件
	 */
	@PermissionInfo(name = "政策法规分页列表", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_query")
	@ApiOperation(value = "政策法规分页列表")
	@PostMapping(value = "/policies/query")
	public ResultVO<PageVO<PoliciesVO>> getPolices(@RequestBody QueryPoliciesDTO queryPoliciesDTO) {
		PageVO<PoliciesVO> policesList = policiesService.getPolicesList(queryPoliciesDTO);
		return ResultVO.ok(policesList);
	}

	/**
	 * 新增政策法规接口
	 *
	 * @param policiesCombinationDTO 政策法规条件组合
	 */
	@PermissionInfo(name = "政策法规新增", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_insert")
	@ApiOperation(value = "政策法规新增")
	@PostMapping(value = "/policies")
	public ResultVO<PoliciesCombinationDTO> savePolicies(
			@Validated(ValidationGroup.Create.class) @ApiParam("政策法规组合") @RequestBody
					PoliciesCombinationDTO policiesCombinationDTO) {
		policiesService.savePolicies(policiesCombinationDTO, ProducerUserUtil.getCurrentUser().getId());
		return ResultVO.ok(policiesCombinationDTO);
	}


	/**
	 * 根据id获取政策法规详情
	 *
	 * @param id 政策法规id
	 */
	@PermissionInfo(name = "政策法规详情", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_detail")
	@ApiOperation(value = "政策法规详情")
	@GetMapping(value = "/policies/detail/{id}")
	public ResultVO<PoliciesCombinationDTO> getPoliciesById(
			@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
		PoliciesCombinationDTO policiesDetailVO = policiesService.getPoliciesById(id);
		return ResultVO.ok(policiesDetailVO);
	}

	/**
	 * 修改政策法规
	 *
	 * @param policiesCombinationDTO 政策法规条件组合
	 */
	@PermissionInfo(name = "政策法规修改", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_update")
	@ApiOperation(value = "政策法规修改")
	@PutMapping(value = "/policies")
	public ResultVO<Void> updatePolicies(
			@Validated(ValidationGroup.Update.class) @RequestBody
					PoliciesCombinationDTO policiesCombinationDTO) {
		policiesCombinationDTO.setInputUserId(ProducerUserUtil.getCurrentUser().getId());
		policiesService.updatePolicies(policiesCombinationDTO);
		return ResultVO.ok();
	}

	/**
	 * 政策法规废止
	 *
	 * @param queryAbolishDTO 政策法规废止条件
	 */
	@PermissionInfo(name = "政策法规废止", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_abolish")
	@ApiOperation("政策法规废止")
	@PutMapping(value = "/policies/abolish")
	public ResultVO<Void> abolish(@Validated @RequestBody QueryAbolishDTO queryAbolishDTO) {
		policiesService.abolish(queryAbolishDTO);
		return ResultVO.ok();
	}

	/**
	 * 查询废止信息
	 *
	 * @param id 政策法规id
	 */
	@PermissionInfo(name = "政策法规废止信息查询", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_abolish_detail")
	@ApiOperation("政策法规废止信息查询")
	@GetMapping(value = "/policies/abolish/{id}")
	public ResultVO<PoliciesAbolishVO> getAbolish(
			@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
		PoliciesAbolishVO policiesAbolishVO = policiesService.getAbolish(id);
		return ResultVO.ok(policiesAbolishVO);
	}

	/**
	 * 校验删除政策法规
	 *
	 * @param id 政策法规id
	 */
	@PermissionInfo(name = "政策法规校验删除", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_checkDelete")
	@ApiOperation("政策法规校验删除")
	@GetMapping(value = "/policies/checkDelete/{id}")
	public ResultVO<PoliciesCheckDeleteVO> checkDeletePoliciesById(
			@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
		PoliciesCheckDeleteVO policiesCheckDeleteVO = policiesService.checkDeletePoliciesById(id);
		return ResultVO.ok(policiesCheckDeleteVO);
	}

	/**
	 * 删除政策法规
	 *
	 * @param id 政策法规id
	 */
	@PermissionInfo(name = "政策法规删除", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_delete")
	@ApiOperation("政策法规删除")
	@DeleteMapping(value = "/policies/{id}")
	public ResultVO<Void> confirmDeletePoliciesById(
			@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
		policiesService.confirmDeletePoliciesById(id);
		return ResultVO.ok();
	}

	/**
	 * 校验标题和文号是否重复
	 * @param titleOrDocCode
	 * @return
	 */
	@PermissionInfo(name = "政策法规标题和文号校验", group = PermissionGroup.POLICIES)
	@SaCheckPermission("producer_policies_checkTitleAndDocCode")
	@ApiOperation("政策法规标题和文号校验")
	@GetMapping(value = "/policies/checkTitleAndDocCode")
	public ResultVO<Boolean> checkTitleAndDocCode(
			@Validated @RequestParam(value = "titleOrDocCode") String titleOrDocCode) {
		Boolean aBoolean = policiesService.checkTitleAndDocCode(titleOrDocCode);
		return ResultVO.ok(aBoolean);
	}


	/**
	 * 关联政策（模糊查询，政策法规）
	 */
	@PermissionInfo(name = "关联政策查询", group = PermissionGroup.POLICIES_EXPLAIN)
	@SaCheckPermission("producer_relatedPolicies_query")
	@ApiOperation("关联政策查询")
	@PostMapping("/policies/relatedPolicies/query")
	public ResultVO<PageVO<PoliciesTitleVO>> fuzzyQuery(@RequestBody KeywordPageQueryDTO keywordPageQueryDTO) {
		PageVO<PoliciesTitleVO> pageVO = policiesService.fuzzyQuery(keywordPageQueryDTO);
		return ResultVO.ok(pageVO);
	}


}

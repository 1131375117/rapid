package cn.huacloud.taxpreference.controllers.producer;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesCombinationDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryAbolishDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
     * @return
     */
    @PermissionInfo(name = "政策法规列表查询", group = PermissionGroup.POLICIES)
    @SaCheckPermission("producer_policies_query")
    @ApiOperation(value = "政策法规列表查询")
    @PostMapping(value = "/policies/query")
    public ResultVO<PageVO<PoliciesVO>> getPolices(@RequestBody QueryPoliciesDTO queryPoliciesDTO) {
        PageVO<PoliciesVO> policesList = policiesService.getPolicesList(queryPoliciesDTO);
        return ResultVO.ok(policesList);

    }

    /**
     * 新增政策法规接口
     *
     * @param policiesCombinationDTO 政策法规条件组合
     * @return
     */
    @PermissionInfo(name = "政策法规新增", group = PermissionGroup.POLICIES)
    @SaCheckPermission("producer_policies_insert")
    @ApiOperation(value = "政策法规新增")
    @PostMapping(value = "/policies/insert")
    public ResultVO<Void> insertPolicies(@Validated(ValidationGroup.Create.class) @RequestBody PoliciesCombinationDTO policiesCombinationDTO) {
        policiesService.insertPolicies(policiesCombinationDTO, UserUtil.getCurrentUser().getId());
        return ResultVO.ok();
    }

    /**
     * 根据id获取政策法规详情
     *
     * @param id 政策法规id
     * @return
     */
    @PermissionInfo(name = "根据id获取政策法规详情", group = PermissionGroup.POLICIES)
    @SaCheckPermission("producer_policies_detail")
    @ApiOperation(value = "根据id获取政策法规详情")
    @GetMapping(value = "/policies/detail/{id}")
    public ResultVO<PoliciesDetailVO> getPoliciesById(@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
        PoliciesDetailVO policiesDetailVO = policiesService.getPoliciesById(id);
        return ResultVO.ok(policiesDetailVO);
    }

    /**
     * 修改政策法规
     *
     * @param policiesCombinationDTO 政策法规条件组合
     * @return
     */
    @PermissionInfo(name = "修改政策法规", group = PermissionGroup.POLICIES)
    @SaCheckPermission("producer_policies_update")
    @ApiOperation(value = "修改政策法规")
    @PutMapping(value = "/policies/update")
    public ResultVO<Void> updatePolicies(@Validated(ValidationGroup.Update.class) @RequestBody PoliciesCombinationDTO policiesCombinationDTO) {
        policiesService.updatePolicies(policiesCombinationDTO);
        return ResultVO.ok();
    }

    /**
     * 政策法规废止
     *
     * @param queryAbolishDTO 政策法规废止条件
     * @return
     */
    @PermissionInfo(name = "政策法规废止", group = PermissionGroup.POLICIES)
    @SaCheckPermission("producer_policies_abolish")
    @ApiOperation("政策法规废止")
    @PutMapping(value = "/policies/abolish/update")
    public ResultVO<Void> abolish(@RequestBody QueryAbolishDTO queryAbolishDTO) {
        policiesService.abolish(queryAbolishDTO);
        return ResultVO.ok();
    }

    /**
     * 查询废止信息
     *
     * @param id 政策法规id
     * @return
     */
    @PermissionInfo(name = "查询政策法规废止信息", group = PermissionGroup.POLICIES)
    @SaCheckPermission("producer_policies_abolish_detail")
    @ApiOperation("查询政策法规废止信息")
    @PostMapping(value = "/policies/abolish/{id}")
    public ResultVO<PoliciesAbolishVO> getAbolish(@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
        PoliciesAbolishVO policiesAbolishVO = policiesService.getAbolish(id);
        return ResultVO.ok(policiesAbolishVO);
    }

    /**
     * 删除政策法规
     *
     * @param id 政策法规id
     * @return
     */
    @PermissionInfo(name = "删除政策法规", group = PermissionGroup.POLICIES)
    @SaCheckPermission("producer_policies_delete")
    @ApiOperation("删除政策法规")
    @DeleteMapping(value = "/policies/{id}")
    public ResultVO<Void> deletePoliciesById(@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
        policiesService.deletePoliciesById(id);
        return ResultVO.ok();
    }
}
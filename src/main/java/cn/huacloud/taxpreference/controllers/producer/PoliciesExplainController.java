package cn.huacloud.taxpreference.controllers.producer;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 政策解读接口
 *
 * @author wuxin
 */
@Api(tags = "政策解读")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PoliciesExplainController {

    private final PoliciesExplainService policiesExplainService;

    /**
     * 政策解读列表查询接口
     * 关键字： 标题
     * 筛选条件：关联政策、来源、发布时间（区间）
     * 根据发布时间和更新时间排序
     * 分页
     */
    @PermissionInfo(name = "政策解读分页列表", group = PermissionGroup.POLICIES_EXPLAIN)
    @SaCheckPermission("producer_policiesExplain_query")
    @ApiOperation("政策解读分页列表")
    @PostMapping(value = "/policiesExplain/query")
    public ResultVO<PageVO<PoliciesExplainDetailVO>> getPoliciesExplainList(@RequestBody QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
        queryPoliciesExplainDTO.paramReasonable();
        PageVO<PoliciesExplainDetailVO> policiesExplainPageVO = policiesExplainService.getPoliciesExplainList(queryPoliciesExplainDTO);
        return ResultVO.ok(policiesExplainPageVO);
    }

    /**
     * 新增政策解读接口
     * 标题、来源、发布日期、正文
     */
    @PermissionInfo(name = "政策解读新增", group = PermissionGroup.POLICIES_EXPLAIN)
    @SaCheckPermission("producer_policiesExplain_insert")
    @ApiOperation("政策解读新增")
    @PostMapping(value = "/policiesExplain")
    public ResultVO<PoliciesExplainDTO> insertPoliciesExplain(@Validated(ValidationGroup.Create.class) @RequestBody PoliciesExplainDTO policiesExplainDTO) {

        policiesExplainService.insertPoliciesExplain(policiesExplainDTO, UserUtil.getCurrentUser().getId());
        return ResultVO.ok(policiesExplainDTO);
    }

    /**
     * 关联政策（模糊查询，政策法规）
     */
    @PermissionInfo(name = "关联政策查询", group = PermissionGroup.POLICIES_EXPLAIN)
    @SaCheckPermission("producer_relatedPolicies_query")
    @ApiOperation("关联政策查询")
    @PostMapping("/policies/relatedPolicies/query")
    public ResultVO<List<PoliciesTitleVO>> fuzzyQuery(@RequestBody KeywordPageQueryDTO keywordPageQueryDTO) {
        List<PoliciesTitleVO> policiesTitleVOList = policiesExplainService.fuzzyQuery(keywordPageQueryDTO);
        return ResultVO.ok(policiesTitleVOList);
    }

    /**
     * 根据ID获取政策解读详情
     * 政策解读id
     */
    @PermissionInfo(name = "政策解读详情", group = PermissionGroup.POLICIES_EXPLAIN)
    @SaCheckPermission("producer_policiesExplain_detail")
    @ApiOperation("政策解读详情")
    @GetMapping(value = "/policiesExplain/detail/{id}")
    public ResultVO<PoliciesExplainDetailVO> getPoliciesExplainById(@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
        PoliciesExplainDetailVO policiesExplainDetailVO = policiesExplainService.getPoliciesById(id);
        return ResultVO.ok(policiesExplainDetailVO);

    }

    /**
     * 修改政策解读
     * 政策解读id
     */
    @PermissionInfo(name = "政策解读修改", group = PermissionGroup.POLICIES_EXPLAIN)
    @SaCheckPermission("producer_policiesExplain_update")
    @ApiOperation("政策解读修改")
    @PutMapping(value = "/policesExplain")
    public ResultVO<Void> updatePolicesExplain(@Validated(ValidationGroup.Update.class) @RequestBody PoliciesExplainDTO policiesExplainDTO) {
        policiesExplainService.updatePolicesExplain(policiesExplainDTO);
        return ResultVO.ok();

    }

    /**
     * 删除政策解读
     * 政策解读id
     */
    @PermissionInfo(name = "政策解读删除", group = PermissionGroup.POLICIES_EXPLAIN)
    @SaCheckPermission("producer_policiesExplain_delete")
    @ApiOperation("政策解读删除")
    @DeleteMapping("/policiesExplain/{id}")
    public ResultVO<Void> deletePoliciesById(@Validated @NotEmpty(message = "id不能为空") @PathVariable("id") Long id) {
        policiesExplainService.deletePoliciesById(id);
        return ResultVO.ok();
    }

}

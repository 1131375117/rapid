package cn.huacloud.taxpreference.controllers.producer;

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
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 政策解读接口
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
    @PermissionInfo(name = "政策解读列表查询", group = PermissionGroup.POLICIES_EXPLAIN)
    @ApiOperation("政策解读列表查询")
    @PostMapping(value = "/policiesExplainList")
    public ResultVO<PageVO<PoliciesExplainVO>> getPoliciesExplainList(@RequestBody QueryPoliciesExplainDTO queryPoliciesExplainDTO){

        PageVO<PoliciesExplainVO> policiesExplainVOPageVO = policiesExplainService.getPoliciesExplainList(queryPoliciesExplainDTO);
        return ResultVO.ok(policiesExplainVOPageVO);
    }

    /**
     * 新增政策解读接口
     * 标题、来源、发布日期、正文
     */
    @PermissionInfo(name = "新增政策解读", group = PermissionGroup.POLICIES_EXPLAIN)
    @ApiOperation("新增政策解读")
    @PostMapping(value = "/policiesExplain/insert")
    public ResultVO<Void> insertPoliciesExplain(@Validated(ValidationGroup.Create.class)@RequestBody PoliciesExplainDTO policiesExplainDTO){


        policiesExplainService.insertPoliciesExplain(policiesExplainDTO, UserUtil.getCurrentUser().getId());
        return ResultVO.ok();
    }

    /**
     * 关联政策（模糊查询，政策法规）
     */
    @PostMapping("/policies/query")
    public ResultVO<List<PoliciesTitleVO>> fuzzyQuery(@RequestBody KeywordPageQueryDTO keywordPageQueryDTO){
        List<PoliciesTitleVO> policiesTitleVOList = policiesExplainService.fuzzyQuery(keywordPageQueryDTO);
        return ResultVO.ok(policiesTitleVOList);
    }

    /**
     * 根据ID获取政策解读详情
     * 政策解读id
     */
    @PermissionInfo(name = "根据id获取政策解读详情", group = PermissionGroup.POLICIES_EXPLAIN)
    @ApiOperation("根据id获取政策解读详情")
    @GetMapping(value = "/policiesById/{id}")
    public ResultVO<PoliciesExplainDetailVO> getPoliciesById(@PathVariable("id") Long id){
        PoliciesExplainDetailVO policiesExplainDetailVO =policiesExplainService.getPoliciesById(id);
        return ResultVO.ok(policiesExplainDetailVO);

    }

    /**
     * 修改政策解读
     * 政策解读id
     */
    @PermissionInfo(name = "修改政策解读", group = PermissionGroup.POLICIES_EXPLAIN)
    @ApiOperation("修改政策解读")
    @PutMapping(value = "/policesExplain/update")
    public ResultVO<Void> updatePolicesExplain(@Validated(ValidationGroup.Update.class)@RequestBody PoliciesExplainDTO policiesExplainDTO){
        policiesExplainService.updatePolicesExplain(policiesExplainDTO);
        return ResultVO.ok();

    }

    /**
     * 删除政策解读
     * 政策解读id
     */
    @PermissionInfo(name = "删除政策解读", group = PermissionGroup.POLICIES_EXPLAIN)
    @ApiOperation("删除政策解读")
    @DeleteMapping("/policies/delete/{id}")
    public ResultVO<Void> deletePoliciesById(@PathVariable("id") Long id){
        policiesExplainService.deletePoliciesById(id);
        return ResultVO.ok();
    }

    /**
     * 文件上传
     */

}

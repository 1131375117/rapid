package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryAbolishDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 政策法规接口
 *
 * @author wuxin
 */
@Api(tags = "政策法规")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PoliciesController {

    private final PoliciesService policiesService;

    /**
     * 政策列表查询接口
     * 关键字： 标题、文号二选一
     * 筛选条件：税种种类（需查）、纳税人资格认定类型（需查）、适用企业类型（需查）
     * 适用行业（需查）、所属区域（需查）、有效性（需查）、发布时间（区间）
     * 根据发布时间和更新时间排序
     */
    @PermissionInfo(name = "政策法规列表查询", group = PermissionGroup.POLICIES)
    @ApiOperation(value = "政策法规列表查询")
    @PostMapping(value = "/Policies")
    public ResultVO<PageVO<PoliciesVO>> getPolices(@RequestBody QueryPoliciesDTO queryPoliciesDTO) {
        PageVO<PoliciesVO> policesList = policiesService.getPolicesList(queryPoliciesDTO);
        //返回结果
        return ResultVO.ok(policesList);

    }

    /**
     * 新增政策法规接口：
     * 标题（需查重）、文号（需查重）、所属区域（需查）、来源
     * 所属税种（需查）、纳税人资格认定类型（需查）、
     * 使用企业类型（需查）、适用行业（需查）、
     * 有效性、发布日期、摘要、正文
     * 需要在service层调用解读和问答的方法
     * 提交进行标题和文号的查重，失败提示“该标题或文号已存在”
     */
    /**
     * 新增政策法规接口
     * @param policiesListDTO
     * @return
     */
    @PermissionInfo(name = "政策法规新增", group = PermissionGroup.POLICIES)
    @ApiOperation(value = "政策法规新增")
    @PostMapping(value = "/policies/insert")
    public ResultVO<Void> insertPolicies(@Validated(ValidationGroup.Create.class)@RequestBody PoliciesListDTO policiesListDTO) {
        policiesService.insertPolicies(policiesListDTO, UserUtil.getCurrentUser().getId());
        //返回结果
        return ResultVO.ok();
    }

    /**
     * 根据id获取政策法规详情
     * 政策法规id
     *
     * @return
     */
    @PermissionInfo(name = "根据id获取政策法规详情", group = PermissionGroup.POLICIES)
    @ApiOperation(value = "根据id获取政策法规详情")
    @GetMapping(value = "/PoliciesById/{id}")
    public ResultVO<PoliciesDetailVO> getPoliciesById(@PathVariable("id") Long id) {
        PoliciesDetailVO policiesDetailVO = policiesService.getPoliciesById(id);
        //返回结果
        return ResultVO.ok(policiesDetailVO);
    }

    /**
     * 修改政策法规
     * 政策法规id
     */
    @PermissionInfo(name = "修改政策法规", group = PermissionGroup.POLICIES)
    @ApiOperation(value = "修改政策法规")
    @PutMapping(value = "/policies/update")
    public ResultVO<Void> updatePolicies(@Validated(ValidationGroup.Update.class)@RequestBody PoliciesListDTO policiesListDTO) {

        policiesService.updatePolicies(policiesListDTO);
        //返回结果
        return ResultVO.ok();
    }

    /**
     * 政策法规废止
     * 政策法规id
     *  全文废止和部分废止
     */
    @PermissionInfo(name = "政策法规废止", group = PermissionGroup.POLICIES)
    @ApiOperation("政策法规废止")
    @PutMapping(value = "/policies/abolish/update")
    public ResultVO<Void> isAbolish(@RequestBody QueryAbolishDTO queryAbolishDTO){
        policiesService.isAbolish(queryAbolishDTO);
        return ResultVO.ok();

    }

    /**
     * 查询废止信息
     */
    @PermissionInfo(name = "政策法规废止", group = PermissionGroup.POLICIES)
    @ApiOperation("查询政策法规废止信息")
    @PostMapping(value = "/policies/abolish/{id}")
    public ResultVO<PoliciesAbolishVO> getAbolish(@PathVariable("id") Long id){
        PoliciesAbolishVO policiesAbolishVO=policiesService.getAbolish(id);
        return ResultVO.ok(policiesAbolishVO);

    }


    /**
     * 删除政策法规
     * 政策法规id
     */
    @PermissionInfo(name = "删除政策法规", group = PermissionGroup.POLICIES)
    @ApiOperation("删除政策法规")
    @DeleteMapping(value = "/policies/{id}")
    public ResultVO<Void> deletePoliciesById(@PathVariable("id") Long id){
        policiesService.deletePoliciesById(id);
        return ResultVO.ok();
    }
}

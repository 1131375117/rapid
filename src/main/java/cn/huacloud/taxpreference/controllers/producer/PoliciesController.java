package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
    @ApiOperation(value = "政策法规列表查询")
    @PostMapping(value = "/Policies")
    public ResultVO<IPage<PoliciesDO>> getPolices(@RequestBody QueryDTO queryDTO) {
        IPage<PoliciesDO> polices = policiesService.getPolices(queryDTO);
        //返回结果
        return ResultVO.ok(polices);

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
    @ApiOperation(value = "政策法规新增")
    @PostMapping(value = "/insertPolicies")
    public ResultVO<Void> insertPolicies(@RequestBody PoliciesListDTO policiesListDTO) {


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
    @ApiOperation(value = "根据id获取政策法规详情")
    @GetMapping(value = "/getPoliciesById/{id}")
    public ResultVO<PoliciesVO> getPoliciesById(@PathVariable("id") Long id) {
        PoliciesVO policiesVO = policiesService.getPoliciesById(id);
        //返回结果
        return ResultVO.ok(policiesVO);
    }

    /**
     * 修改政策法规
     * 政策法规id
     */
    @ApiOperation(value = "政策法规修改")
    @PostMapping(value = "/Policies-")
    public ResultVO<Void> updatePolicies(@RequestBody PoliciesListDTO policiesListDTO) {

        policiesService.updatePolicies(policiesListDTO);
        //返回结果
        return ResultVO.ok();
    }

    /**
     * 政策法规废止
     * 政策法规id
     *  全文废止和部分废止
     */

    /**
     * 删除政策法规
     * 政策法规id
     */
}

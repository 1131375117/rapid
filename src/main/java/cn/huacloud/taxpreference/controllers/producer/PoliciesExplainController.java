package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 新增政策解读接口
     * 标题、来源、发布日期、正文
     */
    @ApiOperation("新增政策解读")
    @PostMapping(value = "PoliciesExplain")
    public ResultVO<Void> insertPoliciesExplain(@RequestBody PoliciesExplainDTO policiesExplainDTO){


        policiesExplainService.insertPoliciesExplain(policiesExplainDTO, UserUtil.getCurrentUser().getId());
        return ResultVO.ok();
    }

    /**
     * 关联政策（模糊查询，政策法规）
     */

    /**
     * 根据ID获取政策解读详情
     * 政策解读id
     */

    /**
     * 修改政策解读
     * 政策解读id
     */

    /**
     * 删除政策解读
     * 政策解读id
     */


}

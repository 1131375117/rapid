package cn.huacloud.taxpreference.controllers.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 政策解读接口
 * @author wuxin
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PoliciesExplainController {

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

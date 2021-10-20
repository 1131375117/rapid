package cn.huacloud.taxpreference.controllers.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 政策法规接口
 * @author wuxin
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PoliciesController {

    /**
     * 政策列表查询接口
     * 关键字： 标题、文号二选一
     * 筛选条件：税种种类（需查）、纳税人资格认定类型（需查）、适用企业类型（需查）
     *          适用行业（需查）、所属区域（需查）、有效性（需查）、发布时间（区间）
     * 根据发布时间和更新时间排序
     */

    /**
     * 新增政策法规接口：
     *    标题（需查重）、文号（需查重）、所属区域（需查）、来源
     *    所属税种（需查）、纳税人资格认定类型（需查）、
     *    使用企业类型（需查）、适用行业（需查）、
     *    有效性、发布日期、摘要、正文
     *    需要在service层调用解读和问答的方法
     *    提交进行标题和文号的查重，失败提示“该标题或文号已存在”
     */

    /**
     * 根据ID获取政策法规详情
     * 政策法规id
     */

    /**
     * 修改政策法规
     * 政策法规id
     */

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

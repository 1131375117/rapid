package cn.huacloud.taxpreference.controllers.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 政策法规接口
 * @author wangkh
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PoliciesController {

    /**
     * 政策列表查询接口
     * 关键字： 标题、文号二选一
     * 筛选条件：税种种类、纳税人资格认定类型、适用企业类型
     *          适用行业、所属区域、有效性、发布时间（区间）
     * 根据发布时间排序
     */

    /**
     * 新增政策法规接口
     */

    /**
     * 根据ID获取政策法规详情
     */

    /**
     * 修改政策法规
     */
}

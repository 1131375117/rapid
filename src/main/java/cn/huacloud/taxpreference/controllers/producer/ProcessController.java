package cn.huacloud.taxpreference.controllers.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程管理接口
 * @author fuhua
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ProcessController {
    /**
     * 税收优惠事项审核列表
     * 参数(可选):优惠事项名称
     */

    /**
     * 税收优惠事项审核详情页面
     *  审核必传参数:税收优惠事项id
     */

    /**
     * 税收优惠事项审核提交
     *  审核必传参数:税收优惠事项id,审核结果
     *        可选:备注审核信息(审核结果不通过时为必填)
     */

}

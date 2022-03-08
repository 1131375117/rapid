package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.SubScribeService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.SubScribeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订阅功能
 *
 * @author fuhua
 **/
@Api(tags = "订阅功能")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class SubScribeController {

    private final SubScribeService subScribeService;

    /**
     * 收藏功能:点击订阅添加到我的订阅,再次点击取消订阅,未登录用户需要先登录
     * 参数列表：docId,订阅类型
     */
    @ApiOperation(value = "点击订阅功能", notes = "返回值boolean表示收藏的当前状态，true：已收藏；false：未收藏")
    @ConsumerUserCheckLogin
    @PutMapping("/subscribe")
    public ResultVO<Boolean> saveOrCancelSubscribe(@RequestBody @Validated SubScribeDTO subScribeDTO) {
        subScribeDTO.setConsumerUserId(ConsumerUserUtil.getCurrentUser().getId());
        Boolean isCollection = subScribeService.saveOrCancelSubscribe(subScribeDTO);
        return ResultVO.ok(isCollection);
    }

}

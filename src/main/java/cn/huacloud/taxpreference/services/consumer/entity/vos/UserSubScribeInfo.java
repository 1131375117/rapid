package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.consumer.SubScribeService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
public abstract class UserSubScribeInfo {

    @Getter
    @Setter
    @ApiModelProperty(value = "是否已订阅",example = "false")
    protected Boolean haveSubscribe = false;

    /**
     * 初始化用户收藏信息
     * @param subscribeType 订阅类型
     */
    public void initUserSubscribe(CollectionType subscribeType) {
        try {
            if (ConsumerUserUtil.isLogin()) {
                haveSubscribe = SpringUtil.getBean(SubScribeService.class)
                        .isUserSubscribe(ConsumerUserUtil.getCurrentUserId(), subscribeType, getId());
            }
        } catch (Exception e) {
            haveSubscribe = false;
        }
    }

    public abstract Long getId();
}

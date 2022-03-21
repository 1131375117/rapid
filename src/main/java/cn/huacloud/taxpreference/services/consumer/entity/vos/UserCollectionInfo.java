package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.consumer.CollectionService;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.consumer.SubScribeService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
public abstract class UserCollectionInfo {

    @Getter
    @Setter
    @ApiModelProperty(value = "是否已收藏",example = "false")
    protected Boolean haveCollection = false;

    /**
     * 初始化用户收藏信息
     * @param collectionType 收藏类型
     */
    public void initUserCollectionInfo(CollectionType collectionType) {
        try {
            if (ConsumerUserUtil.isLogin()) {
                haveCollection = SpringUtil.getBean(CollectionService.class)
                        .isUserCollection(ConsumerUserUtil.getCurrentUserId(), collectionType, getId());
            }
        } catch (Exception e) {
            haveCollection = false;
        }
    }

    public void initOpenAPIUserCollectionInfo(CollectionType collectionType) {
        try {
            Long consumerUserId = OpenApiStpUtil.getConsumerUserId();
            if (consumerUserId != null) {
                haveCollection = SpringUtil.getBean(CollectionService.class)
                        .isUserCollection(consumerUserId, collectionType, getId());
            }
        } catch (Exception e) {
            haveCollection = false;
        }
    }

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

package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.consumer.CollectionService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
public abstract class UserCollectionInfo {

    @Getter
    @Setter
    @ApiModelProperty("是否已收藏")
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

    public abstract Long getId();
}

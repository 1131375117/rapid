package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.CollectionDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.CollectionVO;

/**
 * 收藏服务
 */
public interface CollectionService {

    /**
     * 添加收藏或取消收藏
     *
     * @param collectionDTO
     */
    Boolean saveOrCancelCollection(CollectionDTO collectionDTO);

    /**
     * 展示我的收藏
     *
     * @param pageQueryDTO
     * @param id
     */
    PageVO<CollectionVO> queryCollection(PageQueryDTO pageQueryDTO, Long id);

    /**
     * 用户是否收藏
     * @param consumerUserId 用户ID
     * @param collectionType 收藏类型
     * @param sourceId 源id
     * @return 用户是否收藏
     */
    Boolean isUserCollection(Long consumerUserId, CollectionType collectionType, Long sourceId);
}

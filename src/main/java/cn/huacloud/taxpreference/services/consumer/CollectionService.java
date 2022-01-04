package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.services.common.entity.dtos.CollectionQueryDTO;
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
     * @return 根据返回结果判断是否收藏
     */
    Boolean saveOrCancelCollection(CollectionDTO collectionDTO);

    /**
     * 我的收藏
     * @param pageQueryDTO 查询条件
     * @param userId 用户id
     * @return 收藏集合
     */
    PageVO<CollectionVO> queryCollection(CollectionQueryDTO pageQueryDTO, Long userId);

    /**
     * 用户是否收藏
     * @param consumerUserId 用户ID
     * @param collectionType 收藏类型
     * @param sourceId 源id
     * @return 用户是否收藏
     */
    Boolean isUserCollection(Long consumerUserId, CollectionType collectionType, Long sourceId);
}

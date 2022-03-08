package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.services.consumer.entity.dos.SubscribeDO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.SubScribeDTO;

import java.util.List;

/**
 * 订阅服务
 * @author hua-cloud
 */
public interface SubScribeService {

    /**
     * 添加收藏或取消收藏
     *
     * @param subScribeDTO
     * @return 根据返回结果判断是否收藏
     */
    Boolean saveOrCancelSubscribe(SubScribeDTO subScribeDTO);

    /**
     * 查询
     * @param id
     * @param taxPreference
     * @return
     */
    List<SubscribeDO> selectList(Long id, CollectionType taxPreference);
}

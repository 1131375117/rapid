package cn.huacloud.taxpreference.services.consumer.mapper;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.services.consumer.entity.dos.CollectionDO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.CollectionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 收藏数据mapper
 *
 * @author fuhua
 **/
@Repository
public interface CollectionMapper extends BaseMapper<CollectionDO> {
    /**
     * 查询政策法规收藏
     *
     * @param pageQueryDTO   查询条件
     * @param collectionType 类型
     * @param userId         用户id
     * @return 我的收藏集合
     */
    IPage<CollectionVO> selectPoliciesByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO, @Param("collectionType") CollectionType collectionType, @Param("userId") Long userId);

    /**
     * 查询案例分析收藏
     *
     * @param pageQueryDTO   查询条件
     * @param collectionType 类型
     * @param userId         用户id
     * @return 我的收藏集合
     */
    IPage<CollectionVO> selectCaseAnalysisByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO, @Param("collectionType") CollectionType collectionType, @Param("userId") Long userId);

    /**
     * 查询热门问答收藏
     *
     * @param pageQueryDTO   查询条件
     * @param collectionType 类型
     * @param userId         用户id
     * @return 我的收藏集合
     */
    IPage<CollectionVO> selectFrequentlyAskedQuestionByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO, @Param("collectionType") CollectionType collectionType, @Param("userId") Long userId);

    /**
     * 查询政策解读收藏
     *
     * @param pageQueryDTO   查询条件
     * @param collectionType 类型
     * @param userId         用户id
     * @return 我的收藏集合
     */
    IPage<CollectionVO> selectPoliciesExplainByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO, @Param("collectionType") CollectionType collectionType, @Param("userId") Long userId);

    /**
     * 查询税收优惠收藏
     *
     * @param pageQueryDTO   查询条件
     * @param collectionType 类型
     * @param userId         用户id
     * @return 我的收藏集合
     */
    IPage<CollectionVO> selectTaxPreferenceByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO, @Param("collectionType") CollectionType collectionType, @Param("userId") Long userId);

    IPage<CollectionVO> selectConSultationByCollectionType(@Param("page")Page<CollectionVO> page,@Param("collectionType") CollectionType consultation, @Param("userId") Long userId);
}

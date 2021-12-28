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
    IPage<CollectionVO> selectPoliciesByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO,@Param("collectionType") CollectionType policies, @Param("userId") Long userId);

    IPage<CollectionVO> selectCaseAnalysisByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO,@Param("collectionType") CollectionType collectionType, @Param("userId") Long userId);

    IPage<CollectionVO> selectFrequentlyAskedQuestionByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO,@Param("collectionType") CollectionType frequentlyAskedQuestion, @Param("userId") Long userId);

    IPage<CollectionVO> selectPoliciesExplainByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO,@Param("collectionType") CollectionType policiesExplain, @Param("userId") Long userId);

    IPage<CollectionVO> selectTaxPreferenceByCollectionType(@Param("page") Page<CollectionVO> pageQueryDTO,@Param("collectionType") CollectionType taxPreference, @Param("userId") Long userId);
}

package cn.huacloud.taxpreference.services.common.mapper;

import cn.huacloud.taxpreference.services.common.entity.dos.OperationRecordDO;
import cn.huacloud.taxpreference.services.common.entity.vos.OperationRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 操作记录mapper
 *
 * @author fuhua
 **/
@Repository
public interface OperationRecordMapper extends BaseMapper<OperationRecordDO> {

    IPage<OperationRecordVO> selectCaseAnalysisByDocType(@Param("page") Page<OperationRecordVO> page,@Param("type") String name,@Param("userId") Long userId);

    IPage<OperationRecordVO> selectFrequentlyAskedQuestionByDocType(@Param("page") Page<OperationRecordVO> page, @Param("type") String name,@Param("userId") Long userId);

    IPage<OperationRecordVO> selectPoliciesExplainByDocType(@Param("page") Page<OperationRecordVO> page,@Param("type") String name,@Param("userId") Long userId);

    IPage<OperationRecordVO> selectTaxPreferenceByDocType(@Param("page") Page<OperationRecordVO> page, @Param("type") String name,@Param("userId") Long userId);

    IPage<OperationRecordVO> selectPoliciesByDocType(@Param("page") Page<OperationRecordVO> page, @Param("type") String name,@Param("userId") Long userId);
}

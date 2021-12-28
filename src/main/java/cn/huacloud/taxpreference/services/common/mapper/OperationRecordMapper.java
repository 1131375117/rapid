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
    /**
     * 查询案例分析操作记录
     *
     * @param page   页码
     * @param name   类型
     * @param userId 用户id
     * @return 操作记录集合
     */
    IPage<OperationRecordVO> selectCaseAnalysisByDocType(@Param("page") Page<OperationRecordVO> page, @Param("type") String name, @Param("userId") Long userId);

    /**
     * 查询案例分析操作记录
     *
     * @param page   页码
     * @param name   类型
     * @param userId 用户id
     * @return 操作记录集合
     */
    IPage<OperationRecordVO> selectFrequentlyAskedQuestionByDocType(@Param("page") Page<OperationRecordVO> page, @Param("type") String name, @Param("userId") Long userId);

    /**
     * 查询政策解读操作记录
     *
     * @param page   页码
     * @param name   类型
     * @param userId 用户id
     * @return 操作记录集合
     */
    IPage<OperationRecordVO> selectPoliciesExplainByDocType(@Param("page") Page<OperationRecordVO> page, @Param("type") String name, @Param("userId") Long userId);

    /**
     * 查询税收优惠操作记录
     *
     * @param page   页码
     * @param name   类型
     * @param userId 用户id
     * @return 操作记录集合
     */
    IPage<OperationRecordVO> selectTaxPreferenceByDocType(@Param("page") Page<OperationRecordVO> page, @Param("type") String name, @Param("userId") Long userId);

    /**
     * 查询政策法规操作记录
     *
     * @param page   页码
     * @param name   类型
     * @param userId 用户id
     * @return 操作记录集合
     */
    IPage<OperationRecordVO> selectPoliciesByDocType(@Param("page") Page<OperationRecordVO> page, @Param("type") String name, @Param("userId") Long userId);
}

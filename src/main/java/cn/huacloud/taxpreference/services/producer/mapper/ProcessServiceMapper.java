package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.ProcessDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessListDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程管理
 *
 * @author fuhua
 */
public interface ProcessServiceMapper extends BaseMapper<ProcessDO> {
    /**
     * 修改流程状态
     *
     * @param taxPreferenceId
     */
    void updateByTaxPreferenceId(Long taxPreferenceId);

    /**
     * 查询流程列表
     *
     * @param page
     * @param processListDTO
     * @return IPage
     */
    IPage<ProcessListVO> queryProcessList(@Param("page") Page<ProcessListVO> page, @Param("query") ProcessListDTO processListDTO);

    /**
     * 查询状态
     *
     * @param id
     * @return status
     */
    String selectByTaxPreferenceId(Long id);

    /**
     * 根据税收优惠ID获取最新审批流程
     * @param taxPreferenceId 税收优惠ID
     * @return 流程数据实体
     */
    default ProcessDO getLatestProcess(Long taxPreferenceId) {
        LambdaQueryWrapper<ProcessDO> queryWrapper = Wrappers.lambdaQuery(ProcessDO.class)
                .eq(ProcessDO::getTaxPreferenceId, taxPreferenceId)
                .eq(ProcessDO::getLatestProcess, true);
        List<ProcessDO> processDOS = selectList(queryWrapper);
        if (processDOS.isEmpty()) {
            return null;
        }
        return processDOS.get(0);
    }
}

package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.ProcessDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ProcessListDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ProcessListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

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
}

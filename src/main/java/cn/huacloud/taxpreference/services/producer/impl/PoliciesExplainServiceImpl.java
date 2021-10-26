package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 政策解读服务实现类
 *
 * @author wuxin
 */
@RequiredArgsConstructor
@Service
public class PoliciesExplainServiceImpl implements PoliciesExplainService {

    private final PoliciesExplainMapper policiesExplainMapper;

    private final PoliciesMapper policiesMapper;

    /**
     * 政策解读列表
     *
     * @param queryPoliciesExplainDTO
     * @return
     */
    @Override
    public PageVO<PoliciesExplainVO> getPoliciesExplainList(QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
        Long policiesId = queryPoliciesExplainDTO.getPoliciesId();
        PoliciesDO policiesDO = policiesMapper.selectById(policiesId);
        //模糊查询--政策解读标题
        LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesExplainDTO.getTitle()),
                PoliciesExplainDO::getTitle,
                queryPoliciesExplainDTO.getKeyword());
        //模糊查询--政策法规标题
//        lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesExplainDTO.getTitle()),
//                PoliciesExplainDO::getTitle,
//                queryPoliciesExplainDTO.getTitle());
        //模糊查询--政策解读来源
        lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesExplainDTO.getDocSource())
                , PoliciesExplainDO::getDocSource,
                queryPoliciesExplainDTO.getDocSource());
        //条件查询--发布日期
        lambdaQueryWrapper.ge(!StringUtils.isEmpty(queryPoliciesExplainDTO.getReleaseDate()),
                PoliciesExplainDO::getReleaseDate, queryPoliciesExplainDTO.getStartTime())
                .le(!StringUtils.isEmpty(queryPoliciesExplainDTO.getReleaseDate()),
                        PoliciesExplainDO::getReleaseDate, queryPoliciesExplainDTO.getEndTime());

        //排序--发布时间
        if (QueryPoliciesExplainDTO.SortField.RELEASE_DATE.equals(queryPoliciesExplainDTO.getSortField())) {
            lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesExplainDTO.getReleaseDate()),
                    PoliciesExplainDO::getReleaseDate,
                    queryPoliciesExplainDTO.getReleaseDate()).orderByDesc(PoliciesExplainDO::getReleaseDate);
        }
        //排序--更新时间
        if (QueryPoliciesExplainDTO.SortField.UPDATE_TIME.equals(queryPoliciesExplainDTO.getSortField())) {
            lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesExplainDTO.getUpdateTime()),
                    PoliciesExplainDO::getUpdateTime,
                    queryPoliciesExplainDTO.getUpdateTime()).orderByDesc(PoliciesExplainDO::getUpdateTime);
        }
        //分页
        IPage<PoliciesExplainDO> policiesExplainDOPage = policiesExplainMapper.selectPage(new Page<PoliciesExplainDO>(queryPoliciesExplainDTO.getPageNum(), queryPoliciesExplainDTO.getPageSize()), lambdaQueryWrapper);
        //数据映射
        List<PoliciesExplainVO> records = policiesExplainDOPage.getRecords().stream().map(policiesExplainDO -> {
            PoliciesExplainVO policiesExplainVO = new PoliciesExplainVO();
            //属性拷贝
            BeanUtils.copyProperties(policiesExplainDO, policiesExplainVO);
            return policiesExplainVO;
        }).collect(Collectors.toList());

        return PageVO.createPageVO(policiesExplainDOPage, records);
    }

    /**
     * 新增政策解读
     *
     * @param policiesExplainDTO
     * @param id
     */
    @Override
    public void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long id) {
        PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
        //设置值
        policiesExplainDO.setPoliciesId(policiesExplainDTO.getPoliciesId());
        policiesExplainDO.setInputUserId(id);
        policiesExplainDO.setReleaseDate(LocalDate.now());
        policiesExplainDO.setCreateTime(LocalDateTime.now());
        policiesExplainDO.setUpdateTime(LocalDateTime.now());
        policiesExplainDO.setDeleted(false);
        //转换
        BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
        policiesExplainMapper.insert(policiesExplainDO);
    }

    /**
     * 修改政策解读
     *
     * @param policiesExplainDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePolicesExplain(PoliciesExplainDTO policiesExplainDTO) {
        //修改政策解读
        PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
        BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
        policiesExplainMapper.updateById(policiesExplainDO);
    }

    /**
     * 根据id查询政策解读详情
     *
     * @param id
     * @return
     */
    @Override
    public PoliciesExplainDetailVO getPoliciesById(Long id) {
        PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectById(id);
        PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
        BeanUtils.copyProperties(policiesExplainDO, policiesExplainDetailVO);
        return policiesExplainDetailVO;
    }

    /**
     * 根据id删除政策解读
     *
     * @param id 政策解读id
     */
    @Override
    public void deletePoliciesById(Long id) {
        PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectById(id);
        policiesExplainDO.setDeleted(true);
        policiesExplainMapper.updateById(policiesExplainDO);

    }

    /**
     * 关联政策模糊查询
     *
     * @param keywordPageQueryDTO 关联政策查询条件
     * @return
     */
    @Override
    public List<PoliciesExplainVO> fuzzyQuery(KeywordPageQueryDTO keywordPageQueryDTO) {
        LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(PoliciesExplainDO::getTitle, keywordPageQueryDTO.getKeyword());
        List<PoliciesExplainDO> policiesExplainDOS = policiesExplainMapper.selectList(lambdaQueryWrapper);
        PoliciesExplainVO policiesExplainVO = null;
        ArrayList<PoliciesExplainVO> policiesExplainVOS = new ArrayList<>();
        for (PoliciesExplainDO policiesExplainDO : policiesExplainDOS) {
            policiesExplainVO = new PoliciesExplainVO();
            BeanUtils.copyProperties(policiesExplainDO, policiesExplainVO);
            policiesExplainVOS.add(policiesExplainVO);
        }
        return policiesExplainVOS;

    }
}

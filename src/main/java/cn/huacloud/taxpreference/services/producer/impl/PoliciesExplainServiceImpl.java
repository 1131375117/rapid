package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    public PageVO<PoliciesExplainDetailVO> getPoliciesExplainList(QueryPoliciesExplainDTO queryPoliciesExplainDTO) {

        LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //模糊查询--政策解读标题
        lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesExplainDTO.getTitle()),
                PoliciesExplainDO::getTitle,
                queryPoliciesExplainDTO.getTitle());
        //模糊查询--政策法规标题
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
        List<PoliciesExplainDetailVO> records = policiesExplainDOPage.getRecords().stream().map(policiesExplainDO -> {
            PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
            //属性拷贝
            BeanUtils.copyProperties(policiesExplainDO, policiesExplainDetailVO);
            return policiesExplainDetailVO;
        }).collect(Collectors.toList());

        return PageVO.createPageVO(policiesExplainDOPage, records);
    }


    /**
     * 新增政策解读
     *
     * @param policiesExplainDTO
     * @param userId
     */
    @Override
    public void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long userId) {
        PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
        //转换
        BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
        //设置值
        policiesExplainDO.setPoliciesId(policiesExplainDTO.getPoliciesId());
        policiesExplainDO.setInputUserId(userId);
        policiesExplainDO.setReleaseDate(LocalDate.now());
        policiesExplainDO.setCreateTime(LocalDateTime.now());
        policiesExplainDO.setUpdateTime(LocalDateTime.now());
        policiesExplainDO.setDeleted(false);
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
        //查询政策解读
        PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectById(policiesExplainDTO.getId());
        //参数校验
        if(policiesExplainDO==null){
            throw BizCode._4100.exception();
        }
        //属性拷贝
        BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
        //修改政策解读
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
        //根据政策解读id查询
        PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectById(id);
        PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
        //属性拷贝
        BeanUtils.copyProperties(policiesExplainDO, policiesExplainDetailVO);
        //返回结果
        return policiesExplainDetailVO;
    }

    /**
     * 根据id删除政策解读
     *
     * @param id 政策解读id
     */
    @Transactional(rollbackFor = Exception.class)
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
    public List<PoliciesTitleVO> fuzzyQuery(KeywordPageQueryDTO keywordPageQueryDTO) {
        //模糊查询-title
        LambdaQueryWrapper<PoliciesDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(PoliciesDO::getTitle, keywordPageQueryDTO.getKeyword());
        List<PoliciesDO> policiesDOS = policiesMapper.selectList(lambdaQueryWrapper);
        //遍历集合

        PoliciesTitleVO policiesTitleVO = null;
        List<PoliciesTitleVO> policiesTitleVOList = new ArrayList<>();
        for (PoliciesDO policiesDO : policiesDOS) {
            policiesTitleVO = new PoliciesTitleVO();
            //属性拷贝
            BeanUtils.copyProperties(policiesDO, policiesTitleVO);
            policiesTitleVOList.add(policiesTitleVO);
        }
        //返回结果
        return policiesTitleVOList;

    }
}

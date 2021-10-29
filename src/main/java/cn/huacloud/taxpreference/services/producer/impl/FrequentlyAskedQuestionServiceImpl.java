package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 热点问答服务实现类
 *
 * @author wuxin
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FrequentlyAskedQuestionServiceImpl implements FrequentlyAskedQuestionService {

    private final FrequentlyAskedQuestionMapper frequentlyAskedQuestionMapper;


    /**
     * 热点问答列表查询
     *
     * @param queryPoliciesExplainDTO 查询条件
     * @return
     */
    @Override
    public PageVO<PoliciesExplainDetailVO> getFrequentlyAskedQuestionList(QueryPoliciesExplainDTO queryPoliciesExplainDTO) {

        LambdaQueryWrapper<FrequentlyAskedQuestionDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //模糊查询--政策解读标题
        lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesExplainDTO.getTitle()),
                FrequentlyAskedQuestionDO::getTitle,
                queryPoliciesExplainDTO.getTitle());
        //模糊查询--政策解读来源
        lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesExplainDTO.getDocSource())
                , FrequentlyAskedQuestionDO::getDocSource,
                queryPoliciesExplainDTO.getDocSource());
        //条件查询--发布日期
        lambdaQueryWrapper.ge(!StringUtils.isEmpty(queryPoliciesExplainDTO.getReleaseDate()),
                FrequentlyAskedQuestionDO::getReleaseDate, queryPoliciesExplainDTO.getStartTime())
                .le(!StringUtils.isEmpty(queryPoliciesExplainDTO.getReleaseDate()),
                        FrequentlyAskedQuestionDO::getReleaseDate, queryPoliciesExplainDTO.getEndTime());

        //排序--发布时间
        if (QueryPoliciesExplainDTO.SortField.RELEASE_DATE.equals(queryPoliciesExplainDTO.getSortField())) {
            lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesExplainDTO.getReleaseDate()),
                    FrequentlyAskedQuestionDO::getReleaseDate,
                    queryPoliciesExplainDTO.getReleaseDate()).orderByDesc(FrequentlyAskedQuestionDO::getReleaseDate);
        }
        //排序--更新时间
        if (QueryPoliciesExplainDTO.SortField.UPDATE_TIME.equals(queryPoliciesExplainDTO.getSortField())) {
            lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesExplainDTO.getUpdateTime()),
                    FrequentlyAskedQuestionDO::getUpdateTime,
                    queryPoliciesExplainDTO.getUpdateTime()).orderByDesc(FrequentlyAskedQuestionDO::getUpdateTime);
        }
        //分页
        IPage<FrequentlyAskedQuestionDO> FrequentlyAskedQuestionDOPage = frequentlyAskedQuestionMapper.selectPage(new Page<FrequentlyAskedQuestionDO>(queryPoliciesExplainDTO.getPageNum(), queryPoliciesExplainDTO.getPageSize()), lambdaQueryWrapper);
        //数据映射
        List<PoliciesExplainDetailVO> records = FrequentlyAskedQuestionDOPage.getRecords().stream().map(frequentlyAskedQuestionDO -> {
            PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
            //属性拷贝
            BeanUtils.copyProperties(frequentlyAskedQuestionDO, policiesExplainDetailVO);
            return policiesExplainDetailVO;
        }).collect(Collectors.toList());

        return PageVO.createPageVO(FrequentlyAskedQuestionDOPage, records);
    }

    /**
     * 新增热点问答
     *
     * @param frequentlyAskedQuestionDTOS
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertFrequentlyAskedQuestion(List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOS, Long id) {
        log.info("新增热点问答dto={}",frequentlyAskedQuestionDTOS);
        for (FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO : frequentlyAskedQuestionDTOS) {
            FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = new FrequentlyAskedQuestionDO();
            BeanUtils.copyProperties(frequentlyAskedQuestionDTO, frequentlyAskedQuestionDO);
            frequentlyAskedQuestionDO.setInputUserId(id);
            frequentlyAskedQuestionDO.setReleaseDate(LocalDate.now());
            frequentlyAskedQuestionDO.setCreateTime(LocalDateTime.now());
            frequentlyAskedQuestionDO.setUpdateTime(LocalDateTime.now());
            frequentlyAskedQuestionDO.setDeleted(false);
            log.info("新增热点问答对象={}",frequentlyAskedQuestionDO);
            frequentlyAskedQuestionMapper.insert(frequentlyAskedQuestionDO);
        }
    }

    /**
     * 修改热点问答
     *
     * @param frequentlyAskedQuestionDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFrequentlyAskedQuestion(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
        //查询热点问答
        FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = frequentlyAskedQuestionMapper.selectById(frequentlyAskedQuestionDTO.getId());
        //参数校验
        if (frequentlyAskedQuestionDO == null) {
            throw BizCode._4100.exception();
        }
        //属性拷贝
        BeanUtils.copyProperties(frequentlyAskedQuestionDTO, frequentlyAskedQuestionDO);
        //修改热点问答
        frequentlyAskedQuestionMapper.updateById(frequentlyAskedQuestionDO);
    }
}

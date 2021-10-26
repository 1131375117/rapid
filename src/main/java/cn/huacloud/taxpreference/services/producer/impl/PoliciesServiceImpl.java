package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.*;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferencePoliciesMapper;
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
import java.util.List;
import java.util.stream.Collectors;


/**
 * 政策法规服务实现类
 *
 * @author wuxin
 */
@RequiredArgsConstructor
@Service
public class PoliciesServiceImpl implements PoliciesService {


    private final PoliciesMapper policiesMapper;

    private final PoliciesExplainService policiesExplainService;

    private final FrequentlyAskedQuestionService frequentlyAskedQuestionService;

    private final TaxPreferencePoliciesMapper taxPreferencePoliciesMapper;

    private final TaxPreferenceMapper taxPreferenceMapper;

    /**
     * 政策列表查询
     *
     * @param queryPoliciesDTO
     * @return
     */
    @Override
    public PageVO<PoliciesVO> getPolicesList(QueryPoliciesDTO queryPoliciesDTO) {

        LambdaQueryWrapper<PoliciesDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //模糊查询标题
        if(QueryPoliciesDTO.KeyWordField.TITLE.equals(queryPoliciesDTO.getKeyWordField())){
            lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesDTO.getTitle()),PoliciesDO::getTitle, queryPoliciesDTO.getKeyword());
        }
        //模糊查询文号
        if(QueryPoliciesDTO.KeyWordField.DOC_CODE.equals(queryPoliciesDTO.getKeyWordField())){
            lambdaQueryWrapper.like(!StringUtils.isEmpty(queryPoliciesDTO.getDocCode()),
                    PoliciesDO::getDocCode,
                    queryPoliciesDTO.getKeyword());
        }
        //条件查询--所属税种码值
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesDTO.getTaxCategoriesCode()),
                PoliciesDO::getTaxCategoriesCode,
                queryPoliciesDTO.getTaxCategoriesCode());
        //条件查询--纳税人资格认定类型码值
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesDTO.getTaxpayerIdentifyTypeCode()),
                PoliciesDO::getTaxpayerIdentifyTypeCode,
                queryPoliciesDTO.getTaxpayerIdentifyTypeCode());
        //条件查询--适用企业类型码值
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesDTO.getEnterpriseTypeCode()),
                PoliciesDO::getEnterpriseTypeCode,
                queryPoliciesDTO.getEnterpriseTypeCode());
        //条件查询--适用行业码值
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesDTO.getAreaCode()),
                PoliciesDO::getIndustryCode,
                queryPoliciesDTO.getIndustryCode());
        //条件查询--所属区域码值
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesDTO.getAreaCode()),
                PoliciesDO::getAreaCode,
                queryPoliciesDTO.getAreaCode());
        //条件查询--有效性
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesDTO.getValidity()),
                PoliciesDO::getValidity, queryPoliciesDTO.getValidity());
        //条件查询--发布日期
        lambdaQueryWrapper.ge(!StringUtils.isEmpty(queryPoliciesDTO.getReleaseDate()),
                PoliciesDO::getReleaseDate, queryPoliciesDTO.getStartTime())
                .le(!StringUtils.isEmpty(queryPoliciesDTO.getReleaseDate()),
                        PoliciesDO::getReleaseDate,queryPoliciesDTO.getEndTime());
        //排序--发布时间
        if (QueryPoliciesDTO.SortField.RELEASE_DATE.equals(queryPoliciesDTO.getSortField())) {
            lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesDTO.getReleaseDate()),
                    PoliciesDO::getReleaseDate,
                    queryPoliciesDTO.getReleaseDate()).orderByDesc(PoliciesDO::getReleaseDate);
        }
        //排序--更新时间
        if (QueryPoliciesDTO.SortField.UPDATE_TIME.equals(queryPoliciesDTO.getSortField())) {
            lambdaQueryWrapper.eq(!StringUtils.isEmpty(queryPoliciesDTO.getUpdateTime()),
                    PoliciesDO::getReleaseDate,
                    queryPoliciesDTO.getUpdateTime()).orderByDesc(PoliciesDO::getUpdateTime);
        }
        //分页
        IPage<PoliciesDO> policiesDoPage =
                policiesMapper.selectPage(new Page<>(queryPoliciesDTO.getPageNum(),
                        queryPoliciesDTO.getPageSize()), lambdaQueryWrapper);

        //数据映射
        List<PoliciesVO> records = policiesDoPage.getRecords().stream().map(policiesDO -> {
            PoliciesVO policiesVO = new PoliciesVO();
            //属性拷贝
            BeanUtils.copyProperties(policiesDO, policiesVO);
            return policiesVO;
        }).collect(Collectors.toList());
        //返回结果
        return  PageVO.createPageVO(policiesDoPage,records);
    }

    /**
     * 新增政策法规
     *
     * @param policiesListDTO
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertPolicies(PoliciesListDTO policiesListDTO, Long id) {
        //新增政策法规
        PoliciesDO policiesDO = new PoliciesDO();
        BeanUtils.copyProperties(policiesListDTO, policiesDO);
        policiesDO.setInputUserId(id);
        //添加废止状态
        policiesDO.setPoliciesStatus(policiesListDTO.getPoliciesStatus());
        //添加发布时间
        policiesDO.setReleaseDate(LocalDate.now());
        //添加创建时间
        policiesDO.setCreateTime(LocalDateTime.now());
        //添加更新时间
        policiesDO.setUpdateTime(LocalDateTime.now());
        //设置删除
        policiesDO.setDeleted(false);
        policiesMapper.insert(policiesDO);
        //新增政策解读
        PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
        BeanUtils.copyProperties(policiesListDTO, policiesExplainDTO);
        policiesExplainDTO.setPoliciesId(policiesDO.getId());
        policiesExplainService.insertPoliciesExplain(policiesExplainDTO, id);
        //新增热点问答
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO = new FrequentlyAskedQuestionDTO();
        BeanUtils.copyProperties(policiesListDTO, frequentlyAskedQuestionDTO);
        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(frequentlyAskedQuestionDTO, id);
    }

    /**
     * 根据政策法规id获取详细信息
     *
     * @param id
     * @return
     */
    @Override
    public PoliciesDetailVO getPoliciesById(Long id) {
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        //参数校验
        if(policiesDO==null){
            throw BizCode._4100.exception();
        }
        PoliciesDetailVO policiesDetailVO = new PoliciesDetailVO();
        //属性拷贝
        BeanUtils.copyProperties(policiesDO, policiesDetailVO);
        return policiesDetailVO;
    }

    /**
     * 修改政策法规
     *
     * @param policiesListDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePolicies(PoliciesListDTO policiesListDTO) {
        //修改政策法规
        PoliciesDO policiesDO = new PoliciesDO();
        BeanUtils.copyProperties(policiesListDTO, policiesDO);
        policiesMapper.updateById(policiesDO);
        //修改政策解读
        PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
        BeanUtils.copyProperties(policiesListDTO, policiesExplainDTO);
        policiesExplainService.updatePolicesExplain(policiesExplainDTO);
        //修改热点问答
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO = new FrequentlyAskedQuestionDTO();
        BeanUtils.copyProperties(policiesListDTO, frequentlyAskedQuestionDTO);
        frequentlyAskedQuestionService.updateFrequentlyAskedQuestion(frequentlyAskedQuestionDTO);

    }

    /**
     * 删除政策法规
     *
     * @param id
     */
    @Override
    public void deletePoliciesById(Long id) {
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        policiesDO.setDeleted(true);
        policiesMapper.updateById(policiesDO);
    }

    /**
     * 政策法规废止
     *
     * @param queryAbolishDTO 政策法规废止参数
     */
    @Override
    public void isAbolish(QueryAbolishDTO queryAbolishDTO) {
        Long id = queryAbolishDTO.getId();
        TaxPreferencePoliciesDO taxPreferencePoliciesDO = taxPreferencePoliciesMapper.selectById(id);
        Long taxPreferenceId = taxPreferencePoliciesDO.getTaxPreferenceId();
        TaxPreferenceDO taxPreferenceDO = taxPreferenceMapper.selectById(taxPreferenceId);
        PoliciesDO policiesDO = policiesMapper.selectById(id);

        //参数校验
        if(policiesDO==null){
            throw BizCode._4100.exception();
        }
        if(PoliciesStatusEnum.FULL_TEXT_REPEAL.equals(policiesDO.getPoliciesStatus())){
            policiesDO.setPoliciesStatus(PoliciesStatusEnum.FULL_TEXT_REPEAL.getValue());
        }
        if(PoliciesStatusEnum.PARTIAL_REPEAL.equals(policiesDO.getPoliciesStatus())){
            policiesDO.setPoliciesStatus(PoliciesStatusEnum.PARTIAL_REPEAL.getValue());
            //设置税收优惠的有效性
//            taxPreferenceDO.setTaxPreferenceStatus();
        }
        policiesMapper.updateById(policiesDO);
    }

    /**
     * 查询废止信息
     * @param id
     */
    @Override
    public PoliciesAbolishVO getAbolish(Long id) {
        //查询政策法规
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        //查询税收优惠政策关联表
        TaxPreferencePoliciesDO taxPreferencePoliciesDO = taxPreferencePoliciesMapper.selectById(id);
        //查询税收优惠
        Long taxPreferenceId = taxPreferencePoliciesDO.getTaxPreferenceId();
        TaxPreferenceDO taxPreferenceDO = taxPreferenceMapper.selectById(taxPreferenceId);
        //设置返回结果值
        PoliciesAbolishVO policiesAbolishVO = new PoliciesAbolishVO();
        policiesAbolishVO.setPoliciesStatus(policiesDO.getPoliciesStatus());
        policiesAbolishVO.setAbolishNote(policiesDO.getAbolishNote());
        policiesAbolishVO.setTaxPreferenceName(taxPreferenceDO.getTaxPreferenceName());
        policiesAbolishVO.setValidity(taxPreferenceDO.getValidity());
        //返回结果
        return policiesAbolishVO;
    }
}

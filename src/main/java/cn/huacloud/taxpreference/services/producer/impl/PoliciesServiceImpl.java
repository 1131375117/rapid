package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.taxpreference.SortType;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.*;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPreferencesVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferencePoliciesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 政策法规服务实现类
 *
 * @author wuxin
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PoliciesServiceImpl implements PoliciesService {


    private final PoliciesMapper policiesMapper;

    private final PoliciesExplainService policiesExplainService;

    private final FrequentlyAskedQuestionService frequentlyAskedQuestionService;

    private final TaxPreferencePoliciesMapper taxPreferencePoliciesMapper;

    private final TaxPreferenceMapper taxPreferenceMapper;

    static final String POLICIES_ID = "policies_id";

    /**
     * 政策列表查询
     *
     * @param queryPoliciesDTO
     * @return
     */
    @Override
    public PageVO<PoliciesVO> getPolicesList(QueryPoliciesDTO queryPoliciesDTO) {
        queryPoliciesDTO.paramReasonable();
        Page<PoliciesVO> page = new Page<>(queryPoliciesDTO.getPageNum(), queryPoliciesDTO.getPageSize());
        //获取排序字段
        String sort = getSort(queryPoliciesDTO);
        IPage<PoliciesVO> policiesDoPage = policiesMapper.queryPoliciesVOList(page, queryPoliciesDTO, sort);
        //数据映射
        List<PoliciesVO> records = policiesDoPage.getRecords().stream().map(policiesDO -> {
            PoliciesVO policiesVO = new PoliciesVO();
            //属性拷贝
            BeanUtils.copyProperties(policiesDO, policiesVO);
            return policiesVO;
        }).collect(Collectors.toList());
        //返回结果
        return PageVO.createPageVO(policiesDoPage, records);

    }

    /**
     * 获取排序字段
     */
    private String getSort(QueryPoliciesDTO queryPoliciesDTO) {
        String sort = PoliciesSortType.RELEASE_DATE.getValue();
        if (queryPoliciesDTO.getPoliciesSortType().equals(PoliciesSortType.UPDATE_TIME)) {
            sort = SortType.UPDATE_TIME.name();
        }
        log.info("排序字段sort:{}", sort);
        return sort;
    }

    /**
     * 新增政策法规
     *
     * @param policiesCombinationDTO
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertPolicies(PoliciesCombinationDTO policiesCombinationDTO, Long userId) {
        //新增政策法规
        PoliciesDO policiesDO = new PoliciesDO();
        //设置纳税人、使用企业、适用行业码值
        policiesDO.setTaxpayerIdentifyTypeCodes(org.apache.commons.lang3.StringUtils.join(policiesCombinationDTO.getTaxpayerIdentifyTypeCodes(), ","));
        policiesDO.setEnterpriseTypeCodes(org.apache.commons.lang3.StringUtils.join(policiesCombinationDTO.getEnterpriseTypeCodes(), ","));
        policiesDO.setIndustryCodes(org.apache.commons.lang3.StringUtils.join(policiesCombinationDTO.getIndustryCodes(), ","));
        //进行切分
        String taxpayerIdentifyTypeNames = convert2String(policiesCombinationDTO.getTaxpayerIdentifyTypeCodes());
        String enterpriseTypeCodes = convert2String(policiesCombinationDTO.getEnterpriseTypeCodes());
        String industryNames = convert2String(policiesCombinationDTO.getIndustryCodes());
        //设置纳税人、使用企业、适用行业名称值
        policiesDO.setTaxpayerIdentifyTypeNames(taxpayerIdentifyTypeNames);
        policiesDO.setEnterpriseTypeNames(enterpriseTypeCodes);
        policiesDO.setIndustryNames(industryNames);

        BeanUtils.copyProperties(policiesCombinationDTO, policiesDO);
        policiesDO.setInputUserId(userId);
        //添加废止状态
        policiesDO.setPoliciesStatus(policiesCombinationDTO.getPoliciesStatus());
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
        BeanUtils.copyProperties(policiesCombinationDTO, policiesExplainDTO);
        policiesExplainDTO.setPoliciesId(policiesDO.getId());
        policiesExplainService.insertPoliciesExplain(policiesExplainDTO, userId);
        //新增热点问答--todo
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO = new FrequentlyAskedQuestionDTO();
        BeanUtils.copyProperties(policiesCombinationDTO, frequentlyAskedQuestionDTO);
        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(frequentlyAskedQuestionDTO, userId);
    }

    /**
     * 拼接转换
     */
    @NotNull
    private String convert2String(List<String> industryCodes) {
        Set<String> keySet = new HashSet<>();
        industryCodes.forEach(industryCode -> {
            //todo 通过code查询名称
            keySet.add("a");
        });
        log.info("keySet={}", keySet);
        return org.apache.commons.lang3.StringUtils.join(keySet, ",");
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
        if (policiesDO == null) {
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
     * @param policiesCombinationDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePolicies(PoliciesCombinationDTO policiesCombinationDTO) {

        //修改政策法规
        PoliciesDO policiesDO = policiesMapper.selectById(policiesCombinationDTO.getId());
        //参数校验
        if (policiesDO == null) {
            throw BizCode._4100.exception();
        }
        BeanUtils.copyProperties(policiesCombinationDTO, policiesDO);
        policiesMapper.updateById(policiesDO);
        //修改政策解读
        PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
        BeanUtils.copyProperties(policiesCombinationDTO, policiesExplainDTO);
        policiesExplainService.updatePolicesExplain(policiesExplainDTO);
        //修改热点问答
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO = new FrequentlyAskedQuestionDTO();
        BeanUtils.copyProperties(policiesCombinationDTO, frequentlyAskedQuestionDTO);
        frequentlyAskedQuestionService.updateFrequentlyAskedQuestion(frequentlyAskedQuestionDTO);

    }

    /**
     * 删除政策法规
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
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
    public void abolish(QueryAbolishDTO queryAbolishDTO) {
        //查询政策法规
        Long id = queryAbolishDTO.getId();
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        //参数校验
        if (policiesDO == null) {
            throw BizCode._4100.exception();
        }
        Map<String, Object> columnMap = new HashMap<>(16);
        columnMap.put(POLICIES_ID, policiesDO.getId());
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOS = taxPreferencePoliciesMapper.selectByMap(columnMap);
        Long taxPreferenceId = null;
        for (TaxPreferencePoliciesDO taxPreferencePoliciesDO : taxPreferencePoliciesDOS) {
            taxPreferenceId = taxPreferencePoliciesDO.getTaxPreferenceId();
        }
        LambdaQueryWrapper<TaxPreferenceDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TaxPreferenceDO::getId, taxPreferenceId);
        List<TaxPreferenceDO> taxPreferenceDOS = taxPreferenceMapper.selectList(lambdaQueryWrapper);

        //判断条件--全文废止
        if (PoliciesStatusEnum.FULL_TEXT_REPEAL.equals(policiesDO.getPoliciesStatus())) {
            policiesDO.setPoliciesStatus(PoliciesStatusEnum.FULL_TEXT_REPEAL.getValue());
            //设置政策法规的有效性--todo
            policiesDO.setValidity("全文废止");
            //设置税收优惠的有效性--todo
            for (TaxPreferenceDO taxPreferenceDO : taxPreferenceDOS) {
                taxPreferenceDO.setValidity("失效");
            }
        }
        //判断条件--部分废止
        if (PoliciesStatusEnum.PARTIAL_REPEAL.equals(policiesDO.getPoliciesStatus())) {
            policiesDO.setPoliciesStatus(PoliciesStatusEnum.PARTIAL_REPEAL.getValue());
            //设置政策法规的有效性--todo
            policiesDO.setValidity("部分有效");
            //设置税收优惠的有效性--根据用户自己手动变更--todo

        }
        policiesMapper.updateById(policiesDO);
    }

    /**
     * 查询废止信息
     *
     * @param id
     */
    @Override
    public PoliciesAbolishVO getAbolish(Long id) {
        //查询政策法规
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        //查询税收优惠政策关联表
        Map<String, Object> columnMap = new HashMap<>(16);
        columnMap.put(POLICIES_ID, policiesDO.getId());
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOS = taxPreferencePoliciesMapper.selectByMap(columnMap);
        //查询税收优惠
        Long taxPreferenceId = null;
        for (TaxPreferencePoliciesDO taxPreferencePoliciesDO : taxPreferencePoliciesDOS) {
            taxPreferenceId = taxPreferencePoliciesDO.getTaxPreferenceId();
        }
        LambdaQueryWrapper<TaxPreferenceDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TaxPreferenceDO::getId, taxPreferenceId);
        List<TaxPreferenceDO> taxPreferenceDOS = taxPreferenceMapper.selectList(lambdaQueryWrapper);
        List<String> taxPreferenceNameList = new ArrayList<>();
        for (TaxPreferenceDO taxPreferenceDO : taxPreferenceDOS) {
            String taxCategoriesName = taxPreferenceDO.getTaxCategoriesName();
            taxPreferenceNameList.add(taxCategoriesName);
        }
        //设置返回结果值
        PoliciesAbolishVO policiesAbolishVO = new PoliciesAbolishVO();
        policiesAbolishVO.setPoliciesStatus(policiesDO.getPoliciesStatus());
        policiesAbolishVO.setAbolishNote(policiesDO.getAbolishNote());
        policiesAbolishVO.setNameList(taxPreferenceNameList);
        //返回结果
        return policiesAbolishVO;
    }
}

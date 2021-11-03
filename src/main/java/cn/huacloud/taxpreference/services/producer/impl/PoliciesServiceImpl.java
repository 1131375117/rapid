package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.taxpreference.SortType;
import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.*;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import cn.huacloud.taxpreference.services.producer.entity.vos.*;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferencePoliciesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


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

    private final SysCodeService sysCodeService;

    private final FrequentlyAskedQuestionMapper frequentlyAskedQuestionMapper;

    private final TaxPreferenceService taxPreferenceService;

    private final TaxPreferencePoliciesMapper taxPreferencePoliciesMapper;


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
        //列表查询
        IPage<PoliciesVO> policiesDoPage = policiesMapper.queryPoliciesVOList(page, queryPoliciesDTO, sort);
        log.info("政策法规列表查询对象={}", policiesDoPage);
        //返回结果
        return PageVO.createPageVO(policiesDoPage, policiesDoPage.getRecords());
    }

    /**
     * 获取排序字段
     */
    private String getSort(QueryPoliciesDTO queryPoliciesDTO) {
        String sort = PoliciesSortType.RELEASE_DATE.getValue();
        if (PoliciesSortType.UPDATE_TIME.equals(queryPoliciesDTO.getPoliciesSortType())) {
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
        log.info("新增政策法规组合dto={}", policiesCombinationDTO);
        //校验标题和文号是否存在
        judgeExists(policiesCombinationDTO);
        //新增政策法规
        PoliciesDO policiesDO = new PoliciesDO();
        BeanUtils.copyProperties(policiesCombinationDTO, policiesDO);
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
        //设置所属区域名称
        policiesDO.setAreaName(sysCodeService.getCodeNameByCodeValue(policiesCombinationDTO.getAreaCode()));
        //设置所属税种名称
        policiesDO.setTaxCategoriesName(sysCodeService.getCodeNameByCodeValue(policiesCombinationDTO.getTaxCategoriesCode()));
        //设置标签
        policiesDO.setLabels(StringUtils.join(policiesCombinationDTO.getLabels(), ","));

        log.info("新增政策法规对象={}", policiesDO);
        policiesMapper.insert(policiesDO);
        policiesCombinationDTO.setId(policiesDO.getId());
        //新增政策解读
        PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
        BeanUtils.copyProperties(policiesCombinationDTO.getPoliciesExplainDTO(), policiesExplainDTO);
        policiesExplainDTO.setPoliciesId(policiesDO.getId());
        policiesExplainService.insertPoliciesExplain(policiesExplainDTO, userId);
        //新增热点问答
        List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOList = policiesCombinationDTO.getFrequentlyAskedQuestionDTOList();
        for (FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO : frequentlyAskedQuestionDTOList) {
            Long policiesId = policiesDO.getId();
            frequentlyAskedQuestionDTO.setPoliciesIds(String.valueOf(policiesId));
        }
        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(frequentlyAskedQuestionDTOList, userId);

    }

    /**
     * 校验标题和文号是否重复
     *
     * @param policiesCombinationDTO
     * @return
     */
    private Boolean judgeExists(PoliciesCombinationDTO policiesCombinationDTO) {
        log.info("judgeExists:policiesCombinationDTO={}", policiesCombinationDTO);
        //查询标题和文号
        LambdaQueryWrapper<PoliciesDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!org.springframework.util.StringUtils.isEmpty(policiesCombinationDTO.getTitle()),
                PoliciesDO::getTitle, policiesCombinationDTO.getTitle()).or()
                .eq(!org.springframework.util.StringUtils.isEmpty(policiesCombinationDTO.getDocCode()),
                        PoliciesDO::getDocCode, policiesCombinationDTO.getDocCode());
        List<PoliciesDO> policiesDOS = policiesMapper.selectList(lambdaQueryWrapper);
        //判断是否重复
        if (policiesDOS.size() > 1) {
            throw BizCode._4305.exception();
        }
        //判断修改的条件
        if (policiesDOS.size() == 1
                && !policiesDOS.get(0).getId().equals(policiesCombinationDTO.getId())) {
            throw BizCode._4305.exception();
        }
        return false;


    }

    /**
     * 拼接转换
     */
    @NotNull
    private String convert2String(List<String> industryCodes) {
        Set<String> keySet = new HashSet<>();
        industryCodes.forEach(industryCode -> {
            keySet.add(sysCodeService.getCodeNameByCodeValue(industryCode));
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
        log.info("政策法规详情信息={}", policiesDetailVO);
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
        //判断标题和文号是否重复
        judgeExists(policiesCombinationDTO);
        //修改政策法规
        PoliciesDO policiesDO = policiesMapper.selectById(policiesCombinationDTO.getId());
        //参数校验
        if (policiesDO == null) {
            throw BizCode._4100.exception();
        }
        BeanUtils.copyProperties(policiesCombinationDTO, policiesDO);
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
        log.info("修改政策法规对象={}", policiesDO);
        policiesMapper.updateById(policiesDO);
        //修改政策解读
        PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
        BeanUtils.copyProperties(policiesCombinationDTO, policiesExplainDTO);
        policiesExplainService.updatePolicesExplain(policiesExplainDTO);
        //修改热点问答
        FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO = new FrequentlyAskedQuestionDTO();
        BeanUtils.copyProperties(policiesCombinationDTO, frequentlyAskedQuestionDTO);
        List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOList = policiesCombinationDTO.getFrequentlyAskedQuestionDTOList();
        frequentlyAskedQuestionService.updateFrequentlyAskedQuestion(frequentlyAskedQuestionDTOList);

    }

    /**
     * 校验删除政策法规
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePoliciesById(Long id) {
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        //参数校验
        if (policiesDO == null) {
            throw BizCode._4100.exception();
        }
        policiesDO.setDeleted(true);
        log.info("删除政策法规对象={}", policiesDO);

        //删除税收优惠,根据政策法规id查询税收优惠id--关联表中
        List<TaxPreferenceCountVO> taxPreferenceCountVOS = policiesMapper.selectTaxPreferenceId(policiesDO.getId());
        for (TaxPreferenceCountVO taxPreferenceCountVO : taxPreferenceCountVOS) {
            Long count = taxPreferenceCountVO.getCount();

            //查询优惠事项
            List<TaxPreferenceAbolishVO> taxPreferenceAbolish = taxPreferenceService.getTaxPreferenceAbolish(policiesDO.getId());

            //根据税收优惠id查询关联表中的数据条数


            //判断查询结果是否是多个，多个把关联表的关系删除
            if (count > 1) {
                TaxPreferenceException exception = BizCode._4308.exception(taxPreferenceAbolish);

            } else if (count == 1) {
                //判断查询结果是单个，提示无法删除
                TaxPreferenceException exception = BizCode._4306.exception(taxPreferenceAbolish);
                throw exception;
            } else {
                //政策法规、政策解读、热门问答删除
                throw BizCode._4307.exception();
            }
        }


    }


    @Override
    public void confirmDeletePoliciesById(Long id) {
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        //参数校验
        if (policiesDO == null) {
            throw BizCode._4100.exception();
        }
        policiesDO.setDeleted(true);
        log.info("删除政策法规对象={}", policiesDO);
        //删除税收优惠,根据政策法规id查询税收优惠id--关联表中
        List<TaxPreferenceCountVO> taxPreferenceCountVOS = policiesMapper.selectTaxPreferenceId(policiesDO.getId());
        for (TaxPreferenceCountVO taxPreferenceCountVO : taxPreferenceCountVOS) {
            Long count = taxPreferenceCountVO.getCount();
            if (count > 1) {
                List<TaxPreferenceAbolishVO> taxPreferenceAbolish = taxPreferenceService.getTaxPreferenceAbolish(policiesDO.getId());
                taxPreferenceService.deleteTaxPreferencePolicies(policiesDO.getId());
                TaxPreferenceException exception = BizCode._4308.exception(taxPreferenceAbolish);

            } else if (count == 1) {
                List<TaxPreferenceAbolishVO> taxPreferenceAbolish = taxPreferenceService.getTaxPreferenceAbolish(policiesDO.getId());

                //判断查询结果是单个，提示无法删除
                TaxPreferenceException exception = BizCode._4306.exception(taxPreferenceAbolish);
                throw exception;
            } else {

                deletePolciesExplain(policiesDO);
                //删除热点问答
                deleteFrequentlyAskedQuestion(policiesDO);
                policiesMapper.updateById(policiesDO);
            }
        }
        if(taxPreferenceCountVOS.isEmpty()){
            deletePolciesExplain(policiesDO);
            //删除热点问答
            deleteFrequentlyAskedQuestion(policiesDO);
        }

    }


    private void deleteFrequentlyAskedQuestion(PoliciesDO policiesDO) {
        List<FrequentlyAskedQuestionDO> frequentlyAskedQuestionIds = policiesMapper.selectFrequentlyAskedQuestionId(policiesDO.getId());
        log.info("热点问答id集合={}", frequentlyAskedQuestionIds);
        for (FrequentlyAskedQuestionDO frequentlyAskedQuestionId : frequentlyAskedQuestionIds) {
            ArrayList<String> list = new ArrayList<>();
            List<String> ids = Arrays.asList(frequentlyAskedQuestionId.getPoliciesIds().split(","));
            list.addAll(ids);
            list.remove(String.valueOf(policiesDO.getId()));
            frequentlyAskedQuestionId.setPoliciesIds(StringUtils.join(list, ","));
            frequentlyAskedQuestionMapper.updateById(frequentlyAskedQuestionId);
        }
    }

    private void deletePolciesExplain(PoliciesDO policiesDO) {
        List<Long> policiesExplainIds = policiesMapper.selectExplainId(policiesDO.getId());
        for (Long policiesExplainId : policiesExplainIds) {
            policiesExplainService.deletePoliciesById(policiesExplainId);
        }
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
        //判断条件--全文废止
        if (PoliciesStatusEnum.FULL_TEXT_REPEAL.getValue().equals(queryAbolishDTO.getPoliciesStatus())) {
            //设置政策法规的有效性
            policiesDO.setValidity(ValidityEnum.FULL_TEXT_REPEAL.getValue());
            //设置税收优惠的有效性
        } else if (PoliciesStatusEnum.PARTIAL_REPEAL.getValue().equals(queryAbolishDTO.getPoliciesStatus())) {
            //判断条件--部分废止
            policiesDO.setPoliciesStatus(PoliciesStatusEnum.PARTIAL_REPEAL.getValue());
            //设置政策法规的有效性
            policiesDO.setValidity(ValidityEnum.PARTIAL_VALID.getValue());
        }
        taxPreferenceService.updateStatus(queryAbolishDTO);
        log.info("废止政策法规对象={}", policiesDO);
        policiesMapper.updateById(policiesDO);
    }

    /**
     * 查询废止信息
     *
     * @param id 政策法规id
     */
    @Override
    public PoliciesAbolishVO getAbolish(Long id) {
        //查询政策法规
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        List<TaxPreferenceAbolishVO> taxPreferenceAbolish = taxPreferenceService.getTaxPreferenceAbolish(id);
        //设置返回结果值
        PoliciesAbolishVO policiesAbolishVO = new PoliciesAbolishVO();
        policiesAbolishVO.setPoliciesStatus(policiesDO.getPoliciesStatus());
        policiesAbolishVO.setAbolishNote(policiesDO.getAbolishNote());
        policiesAbolishVO.setTaxPreferenceVOS(taxPreferenceAbolish);
        //返回结果
        log.info("查询政策法规废止的对象={}", policiesAbolishVO);
        return policiesAbolishVO;
    }
}

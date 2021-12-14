package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.constants.TaxpayerTypeConstants;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import cn.huacloud.taxpreference.common.enums.taxpreference.SortType;
import cn.huacloud.taxpreference.common.enums.taxpreference.TaxPreferenceStatus;
import cn.huacloud.taxpreference.common.enums.taxpreference.TaxStatus;
import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dtos.SysCodeStringDTO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;
import cn.huacloud.taxpreference.services.producer.ProcessService;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.*;
import cn.huacloud.taxpreference.services.producer.entity.dtos.*;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import cn.huacloud.taxpreference.services.producer.entity.vos.*;
import cn.huacloud.taxpreference.services.producer.mapper.*;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
import cn.huacloud.taxpreference.sync.es.trigger.impl.TaxPreferenceEventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 优惠政策服务实现类
 *
 * @author fuhua
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaxPreferenceServiceImpl implements TaxPreferenceService {

    private final TaxPreferenceMapper taxPreferenceMapper;
    private final TaxPreferencePoliciesMapper taxPreferencePoliciesMapper;
    private final SubmitConditionMapper submitConditionMapper;
    private final ProcessServiceMapper processServiceMapper;
    private ProcessService processService;
    private final PoliciesMapper policiesMapper;
    private final SysCodeService sysCodeService;
    private final TaxPreferenceEventTrigger taxPreferenceEventTrigger;
    static final String TAX_PREFERENCE_ID = "tax_preference_id";

    @Autowired
    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> insertTaxPreference(TaxPreferenceDTO taxPreferenceDTO, ProducerLoginUserVO currentUser) throws MethodArgumentNotValidException {
        log.info("新增政策法规dto={}", taxPreferenceDTO);
        taxPreferenceDTO.setInputUserId(currentUser.getId());
        // 新增-税收优惠表t_tax_preference
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(taxPreferenceDTO);
        taxPreferenceDO.setCreateTime(LocalDateTime.now());
        log.info("税收优惠新增对象:{}", taxPreferenceDO);
        taxPreferenceMapper.insert(taxPreferenceDO);
        //判断是否需要提交发布申请
        taxPreferenceDTO.setId(taxPreferenceDO.getId());
        isRequireReleased(taxPreferenceDTO, currentUser, taxPreferenceDO);

        // 新增-税收优惠政策法规关联表t_tax_preference_ policies
        insertTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);

        // 新增-申报条件表 t_submit_condition
        insertSubmitConditionDOs(taxPreferenceDTO, taxPreferenceDO);
        return ResultVO.ok();
    }

    /**
     * 是否需要发布
     */
    private void isRequireReleased(TaxPreferenceDTO taxPreferenceDTO, ProducerLoginUserVO currentUser, TaxPreferenceDO taxPreferenceDO) throws MethodArgumentNotValidException {
        if (TaxStatus.SUBMIT.equals(taxPreferenceDTO.getStatus())) {
            // 判断是否存在
            judgeExists(taxPreferenceDTO);
            // 检测纳税人类型和标签管理
            checkLabels(taxPreferenceDTO);
            processService.insertProcessService(new Long[]{(taxPreferenceDO.getId())}, currentUser);
            //同步到es
            taxPreferenceEventTrigger.saveEvent(taxPreferenceDTO.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> updateTaxPreference(TaxPreferenceDTO taxPreferenceDTO, ProducerLoginUserVO currentUser) throws MethodArgumentNotValidException {
        // 判断是否已经发布
        judgeRelease(taxPreferenceDTO.getId());
        // 判断是否在审批中
        processService.judgeProcessIng(taxPreferenceDTO.getId());
        // 判断标签
        checkLabels(taxPreferenceDTO);
        // 修改-税收优惠表t_tax_preference
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(taxPreferenceDTO);
        taxPreferenceMapper.updateById(taxPreferenceDO);
        isRequireReleased(taxPreferenceDTO, currentUser, taxPreferenceDO);

        // 修改政策法规
        updateTaxPreferencePolicy(taxPreferenceDTO, taxPreferenceDO);

        // 修改-申报条件表 t_submit_condition
        updateSubmitConditionByTaxPreferenceId(taxPreferenceDTO);

        return ResultVO.ok();
    }

    @Override
    public ResultVO<PageVO<QueryTaxPreferencesVO>> queryTaxPreferenceList(
            QueryTaxPreferencesDTO queryTaxPreferencesDTO, Long userId) {
        log.info("税收优惠查询条件:queryTaxPreferencesDTO:{}", queryTaxPreferencesDTO);
        Page<QueryTaxPreferencesVO> page =
                new Page<>(queryTaxPreferencesDTO.getPageNum(), queryTaxPreferencesDTO.getPageSize());
        // 获取排序字段
        String sort = getSort(queryTaxPreferencesDTO);
        IPage<QueryTaxPreferencesVO> iPage =
                taxPreferenceMapper.queryTaxPreferenceVOList(page, queryTaxPreferencesDTO, sort, userId);
        List<QueryTaxPreferencesVO> records = iPage.getRecords();
        records.forEach(
                queryTaxPreferencesVO -> {
                    String processStatus =
                            processServiceMapper.selectByTaxPreferenceId(queryTaxPreferencesVO.getId());
                    queryTaxPreferencesVO.setProcessStatus(processStatus == null ? "无" : processStatus);
                });
        PageVO<QueryTaxPreferencesVO> pageVO = PageVO.createPageVO(iPage, iPage.getRecords());
        return ResultVO.ok(pageVO);
    }

    @Override
    public ResultVO<TaxPreferenceVO> queryTaxPreferenceInfo(Long id) {
        log.info("税收优惠详情获取查询条件:id:{}", id);
        TaxPreferenceVO taxPreferenceVO = new TaxPreferenceVO();
        Map<String, Object> columnMap = new HashMap<>(16);
        columnMap.put(TAX_PREFERENCE_ID, id);

        // 根据id查询税收优惠表t_tax_preference信息
        TaxPreferenceDO taxPreferenceDO = taxPreferenceMapper.selectById(id);
        if (taxPreferenceDO == null) {
            throw BizCode._4309.exception();
        }
        BeanUtils.copyProperties(taxPreferenceDO, taxPreferenceVO);
        //设置行业类型和码值
        if (!StringUtils.isEmpty(taxPreferenceDO.getIndustryCodes())) {
            taxPreferenceVO.setIndustryCodes(Arrays.asList(taxPreferenceDO.getIndustryCodes().split(",")));
        } else {
            taxPreferenceVO.setIndustryCodes(new ArrayList<>());
        }
        if (!StringUtils.isEmpty(taxPreferenceDO.getIndustryCodes())) {
            taxPreferenceVO.setIndustryNames(Arrays.asList(taxPreferenceDO.getIndustryNames().split(",")));
        } else {
            taxPreferenceVO.setIndustryNames(new ArrayList<>());
        }

        //设置信用凭证
        if (taxPreferenceDO.getTaxpayerCreditRatings() != null) {
            taxPreferenceVO.setTaxpayerCreditRatings(Arrays.asList(taxPreferenceDO.getTaxpayerCreditRatings().split(",")));
        } else {
            taxPreferenceVO.setTaxpayerCreditRatings(new ArrayList<>());
        }
        //设置企业类型和码值
        if (!StringUtils.isEmpty(taxPreferenceDO.getEnterpriseTypeCodes())) {
            taxPreferenceVO.setEnterpriseTypeCodes(Arrays.asList(taxPreferenceDO.getEnterpriseTypeCodes().split(",")));
        } else {
            taxPreferenceVO.setEnterpriseTypeCodes(new ArrayList<>());
        }
        if (!StringUtils.isEmpty(taxPreferenceDO.getEnterpriseTypeNames())) {
            taxPreferenceVO.setEnterpriseTypeNames(Arrays.asList(taxPreferenceDO.getEnterpriseTypeNames().split(",")));
        } else {
            taxPreferenceVO.setEnterpriseTypeNames(new ArrayList<>());
        }

        //设置标签
        if (!StringUtils.isEmpty(taxPreferenceDO.getLabels())) {
            List<String> labels = Arrays.asList(taxPreferenceDO.getLabels().split(","));
            taxPreferenceVO.setLabels(labels);
        }


        // 查询政策信息
        List<TaxPreferencePoliciesVO> taxPreferencePoliciesVOList =
                getTaxPreferencePoliciesVOS(columnMap);
        taxPreferenceVO.setTaxPreferencePoliciesVOList(taxPreferencePoliciesVOList);

        // 查询申报信息
        List<SubmitConditionDO> submitConditionDOList = submitConditionMapper.selectByMap(columnMap);
        List<SubmitConditionVO> submitConditionVOList = getSubmitConditionVOS(submitConditionDOList);
        taxPreferenceVO.setSubmitConditionVOList(submitConditionVOList);
        log.info("税收优惠详情获取查询结果:taxPreferenceVO:{}", taxPreferenceVO);
        return ResultVO.ok(taxPreferenceVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> deleteTaxPreference(Long[] ids) {
        for (Long id : ids) {
            log.info("删除条件:ids={}", id);
            Map<String, Object> keyMap = new HashMap<>(16);
            keyMap.put(TAX_PREFERENCE_ID, id);
            // 校验是否发布
            judgeRelease(id);
            // 校验是否在审批流程中
            processService.judgeProcessIng(id);
            // 逻辑删除t_tax_preference
            taxPreferenceMapper.updateDeletedById(id);
            // 删除-申报条件t_submit_condition
            submitConditionMapper.deleteByMap(keyMap);
            // 删除政策法规t_tax_preference_policies
            taxPreferencePoliciesMapper.deleteByMap(keyMap);
            //同步到es
            taxPreferenceEventTrigger.deleteEvent(id);
            log.info("id为{}的税收优惠删除成功！", id);
        }

        return ResultVO.ok(null);
    }

    @Override
    @Transactional(rollbackFor = TaxPreferenceException.class)
    public ResultVO<Void> reTaxPreference(Long id) {
        log.info("撤回条件:ids={}", id);
        // 校验发布状态
        checkReleaseStatus(id);
        // 税收优惠撤回
        revokeTaxPreference(id);
        // 撤回之后删除process数据
        // deleteProcess(id);
        //同步到es
        taxPreferenceEventTrigger.deleteEvent(id);
        log.info("id为{}的税收优惠撤回成功！", id);
        return ResultVO.ok();
    }

    /**
     * 校验发布状态
     */
    private void checkReleaseStatus(Long id) {
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(id);
        if (TaxPreferenceStatus.UNRELEASED
                .equals(taxPreferenceDO.getTaxPreferenceStatus())) {
            throw BizCode._4310.exception();
        }
    }

    /**
     * 根据id获取详细信息
     */
    private TaxPreferenceDO getTaxPreferenceDO(Long id) {
        LambdaQueryWrapper<TaxPreferenceDO> queryWrapper =
                Wrappers.lambdaQuery(TaxPreferenceDO.class).eq(TaxPreferenceDO::getId, id);
        return taxPreferenceMapper.selectOne(queryWrapper);
    }

    /**
     * 撤回之后删除process数据
     */
    private void deleteProcess(Long id) {
        LambdaQueryWrapper<ProcessDO> queryWrapper =
                Wrappers.lambdaQuery(ProcessDO.class).eq(ProcessDO::getTaxPreferenceId, id);
        if (id != null) {
            processServiceMapper.delete(queryWrapper);
        }
    }

    /**
     * 税收优惠撤回
     */
    private void revokeTaxPreference(Long id) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        taxPreferenceDO.setId(id);
        taxPreferenceDO.setTaxPreferenceStatus(TaxPreferenceStatus.UNRELEASED);
        taxPreferenceMapper.updateById(taxPreferenceDO);
    }

    /**
     * 获取申报信息
     *
     * @return submitConditionVO
     */
    private List<SubmitConditionVO> getSubmitConditionVOS(
            List<SubmitConditionDO> submitConditionDOS) {
        List<SubmitConditionVO> submitConditionVOList = new ArrayList<>();
        submitConditionDOS.forEach(
                submitConditionDO -> {
                    SubmitConditionVO submitConditionVO = new SubmitConditionVO();
                    BeanUtils.copyProperties(submitConditionDO, submitConditionVO);
                    submitConditionVOList.add(submitConditionVO);
                });
        log.info("申报信息结果:submitConditionVOList={}", submitConditionVOList);
        return submitConditionVOList;
    }

    /**
     * 校验发布状态
     */
    private void judgeRelease(Long id) {
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(id);
        if (TaxPreferenceStatus.RELEASED.equals(taxPreferenceDO.getTaxPreferenceStatus())) {
            throw BizCode._4311.exception();
        }
    }

    private void updateTaxPreferencePolicy(
            TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        // 采取先删除后添加的方式
        HashMap<String, Object> columnMap = new HashMap<>(16);
        columnMap.put(TAX_PREFERENCE_ID, taxPreferenceDTO.getId());
        taxPreferencePoliciesMapper.deleteByMap(columnMap);
        insertTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
    }

    /**
     * 获取排序字段
     */
    private String getSort(QueryTaxPreferencesDTO queryTaxPreferencesDTO) {
        String sort = SortType.CREATE_TIME.getValue();
        if (queryTaxPreferencesDTO.getSortType() == null) {
            return sort;
        }
        if (queryTaxPreferencesDTO.getSortType().equals(SortType.UPDATE_TIME)) {
            sort = SortType.UPDATE_TIME.name();
        }
        log.info("排序字段sort:{}", sort);
        return sort;
    }

    /**
     * 获取政策法规信息
     *
     * @return taxPreferencePoliciesVO
     */
    private List<TaxPreferencePoliciesVO> getTaxPreferencePoliciesVOS(Map<String, Object> columnMap) {
        log.info("获取政策法规信息参数:columnMap={}", columnMap);
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOS =
                taxPreferencePoliciesMapper.selectByMap(columnMap);
        List<TaxPreferencePoliciesVO> taxPreferencePoliciesVOList = new ArrayList<>();
        taxPreferencePoliciesDOS.forEach(
                taxPreferencePoliciesDO -> {
                    TaxPreferencePoliciesVO taxPreferencePoliciesVO = new TaxPreferencePoliciesVO();
                    BeanUtils.copyProperties(taxPreferencePoliciesDO, taxPreferencePoliciesVO);
                    // 获取政策法规详细信息
                    PoliciesDO policiesDO = getPoliciesDO(taxPreferencePoliciesVO);
                    taxPreferencePoliciesVO.setPoliciesName(policiesDO.getTitle());
                    taxPreferencePoliciesVO.setDocCode(policiesDO.getDocCode());
                    taxPreferencePoliciesVOList.add(taxPreferencePoliciesVO);
                });
        log.info("政策法规信结果:taxPreferencePoliciesVOList={}", taxPreferencePoliciesVOList);
        return taxPreferencePoliciesVOList;
    }

    @NotNull
    private PoliciesDO getPoliciesDO(TaxPreferencePoliciesVO taxPreferencePoliciesVO)
            throws TaxPreferenceException {
        PoliciesDO policiesDO = policiesMapper.selectById(taxPreferencePoliciesVO.getPoliciesId());
        if (policiesDO == null) {
            return new PoliciesDO();
        }
        return policiesDO;
    }

    /**
     * 修改税收优惠申报条件
     */
    private void updateSubmitConditionByTaxPreferenceId(TaxPreferenceDTO taxPreferenceDTO) {
        log.info("修改税收优惠申报参数:taxPreferenceDTO={}", taxPreferenceDTO);
        // 采取先删除后添加的方式
        HashMap<String, Object> columnMap = new HashMap<>(16);
        columnMap.put(TAX_PREFERENCE_ID, taxPreferenceDTO.getId());
        submitConditionMapper.deleteByMap(columnMap);
        TaxPreferenceDO taxPreferenceDO = getPreferenceDO(taxPreferenceDTO);
        insertSubmitConditionDOs(taxPreferenceDTO, taxPreferenceDO);
    }

    private TaxPreferenceDO getPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        taxPreferenceDO.setId(taxPreferenceDTO.getId());
        return taxPreferenceDO;
    }

    /**
     * 新增到申报条件表t_submit_condition
     */
    public void insertSubmitConditionDOs(
            TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        log.info(
                "新增到申报条件表t_submit_condition-参数:taxPreferenceDTO={},taxPreferenceDO={}",
                taxPreferenceDTO,
                taxPreferenceDO);
        List<SubmitConditionDTO> submitConditionDTOList = taxPreferenceDTO.getSubmitConditionDTOList();
        if (submitConditionDTOList != null && submitConditionDTOList.size() > 0) {
            for (int i = 0; i < submitConditionDTOList.size(); i++) {
                SubmitConditionDO submitConditionDO = new SubmitConditionDO();
                BeanUtils.copyProperties(submitConditionDTOList.get(i), submitConditionDO);
                submitConditionDO.setTaxPreferenceId(taxPreferenceDO.getId());
                submitConditionDO.setSort((long) i + 1);
                submitConditionMapper.insert(submitConditionDO);
            }
        }
    }

    /**
     * 新增TaxPreferencePoliciesDO
     */
    private void insertTaxPreferencePoliciesDO(
            TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        log.info("填充TaxPreferencePoliciesDO条件-taxPreferenceDTO={}", taxPreferenceDTO);
        // 政策法规对象集合
        List<TaxPreferencePoliciesDTO> taxPreferencePoliciesDTOList =
                taxPreferenceDTO.getTaxPreferencePoliciesDTOList();
        if (taxPreferencePoliciesDTOList != null && taxPreferencePoliciesDTOList.size() > 0) {
            for (int i = 0; i < taxPreferencePoliciesDTOList.size(); i++) {
                TaxPreferencePoliciesDO preferencePoliciesDO = new TaxPreferencePoliciesDO();
                BeanUtils.copyProperties(taxPreferencePoliciesDTOList.get(i), preferencePoliciesDO);
                preferencePoliciesDO.setTaxPreferenceId(taxPreferenceDO.getId());
                preferencePoliciesDO.setSort((long) i + 1);
                log.info("新增TaxPreferencePoliciesDO-preferencePoliciesDO={}", preferencePoliciesDO);
                taxPreferencePoliciesMapper.insert(preferencePoliciesDO);
            }
        }
    }

    /**
     * 填充taxPreferenceDO对象
     */
    private TaxPreferenceDO getTaxPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        log.info("taxPreferenceDTO={}", taxPreferenceDTO);
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        BeanUtils.copyProperties(taxPreferenceDTO, taxPreferenceDO);
        taxPreferenceDO.setDeleted(false);
        taxPreferenceDO.setUpdateTime(LocalDateTime.now());
        taxPreferenceDO.setTaxPreferenceStatus(TaxPreferenceStatus.UNRELEASED);
        SysCodeStringDTO industries = sysCodeService.getSysCodeStringDTO(taxPreferenceDTO.getIndustryCodes(), false);
        SysCodeStringDTO enterprises = sysCodeService.getSysCodeStringDTO(taxPreferenceDTO.getEnterpriseTypeCodes(), false);
        // 收入税种种类名称
        taxPreferenceDO.setTaxCategoriesName(
                sysCodeService.getCodeNameByCodeValue(taxPreferenceDTO.getTaxCategoriesCode()));
        // taxpayer_register_type_name-纳税人登记注册类型名称
        taxPreferenceDO.setTaxpayerRegisterTypeName(
                sysCodeService.getCodeNameByCodeValue(taxPreferenceDTO.getTaxpayerRegisterTypeCode()));
        // 纳税人类型名称-taxpayer_type_name
        taxPreferenceDO.setTaxpayerTypeName(
                sysCodeService.getCodeNameByCodeValue(taxPreferenceDTO.getTaxpayerTypeCode()));
        // 行业code
        taxPreferenceDO.setIndustryCodes(industries.getCodes());
        // 适用企业类型
        taxPreferenceDO.setEnterpriseTypeCodes(enterprises.getCodes());
        // 信用等级
        taxPreferenceDO.setTaxpayerCreditRatings(
                StringUtils.join(taxPreferenceDTO.getTaxpayerCreditRatings(), ","));
        // 设置标签
        taxPreferenceDO.setLabels(StringUtils.join(taxPreferenceDTO.getLabels(), ","));
        // 行业名称
        taxPreferenceDO.setIndustryNames(industries.getNames());
        // 适用企业类型
        taxPreferenceDO.setEnterpriseTypeNames(enterprises.getNames());
        log.info("taxPreferenceDO={}", taxPreferenceDO);
        return taxPreferenceDO;
    }

    /**
     * 拼接转换
     */
    @NotNull
    private String convert2String(List<String> stringList) {
        Set<String> keySet = new HashSet<>();
        stringList.forEach(
                industryCode -> {
                    keySet.add(sysCodeService.getCodeNameByCodeValue(industryCode));
                });
        log.info("keySet={}", keySet);
        return StringUtils.join(keySet, ",");
    }

    /**
     * 判断此税收优惠事项是否存在
     */
    private Boolean judgeExists(TaxPreferenceDTO taxPreferenceDTO) {
        log.info("judgeExists:taxPreferenceDTO={}", taxPreferenceDTO);
        LambdaQueryWrapper<TaxPreferenceDO> queryWrapper =
                Wrappers.lambdaQuery(TaxPreferenceDO.class)
                        .eq(TaxPreferenceDO::getTaxPreferenceName, taxPreferenceDTO.getTaxPreferenceName())
                        .eq(TaxPreferenceDO::getDeleted, false);
        List<TaxPreferenceDO> taxPreferenceDOs = taxPreferenceMapper.selectList(queryWrapper);
        log.info("judgeExists:taxPreferenceDOs={}", taxPreferenceDOs);
        if (taxPreferenceDOs.size() > 1) {
            throw BizCode._4302.exception();
        }
        if (taxPreferenceDOs.size() == 1
                && !taxPreferenceDOs.get(0).getId().equals(taxPreferenceDTO.getId())) {
            throw BizCode._4302.exception();
        }
        return false;
    }

    /**
     * 判断纳税人是否与标签冲突
     */
    private void checkLabels(TaxPreferenceDTO taxPreferenceDTO) {
        if (TaxpayerTypeConstants.YBNSR.equals(taxPreferenceDTO.getTaxpayerTypeCode())) {
            taxPreferenceDTO
                    .getLabels()
                    .forEach(
                            s -> {
                                // 根据标签名称获取code
                                String codeValue = getNamesByCodeValues(s);
                                if (!StringUtils.isEmpty(codeValue)
                                        && TaxpayerTypeConstants.XGMNSR.equals(codeValue)) {
                                    throw BizCode._4313.exception();
                                }
                            });
        }
        if (TaxpayerTypeConstants.XGMNSR.equals(taxPreferenceDTO.getTaxpayerTypeCode())) {
            taxPreferenceDTO
                    .getLabels()
                    .forEach(
                            s -> {
                                // 根据标签名称获取code
                                String codeValue = getNamesByCodeValues(s);
                                if (!StringUtils.isEmpty(codeValue)
                                        && TaxpayerTypeConstants.YBNSR.equals(codeValue)) {
                                    throw BizCode._4313.exception();
                                }
                            });
        }
        if (TaxpayerTypeConstants.JMQY.equals(taxPreferenceDTO.getTaxpayerTypeCode())) {
            taxPreferenceDTO
                    .getLabels()
                    .forEach(
                            s -> {
                                // 根据标签名称获取code
                                String codeValue = getNamesByCodeValues(s);
                                if (!StringUtils.isEmpty(codeValue)
                                        && TaxpayerTypeConstants.FJMQY.equals(codeValue)) {
                                    throw BizCode._4313.exception();
                                }
                            });
        }
        if (TaxpayerTypeConstants.FJMQY.equals(taxPreferenceDTO.getTaxpayerTypeCode())) {
            taxPreferenceDTO
                    .getLabels()
                    .forEach(
                            s -> {
                                // 根据标签名称获取code
                                String codeValue = getNamesByCodeValues(s);
                                if (!StringUtils.isEmpty(codeValue)
                                        && TaxpayerTypeConstants.JMQY.equals(codeValue)) {
                                    throw BizCode._4313.exception();
                                }
                            });
        }
    }

    private String getNamesByCodeValues(String s) {
        SysCodeVO sysCodeVO = sysCodeService.getCodeVOByCodeName(SysCodeType.TAXPAYER_TYPE, s);
        if (sysCodeVO == null) {
            return null;
        }
        return sysCodeVO.getCodeValue();
    }

    /**
     * 根据政策法规废止状态修改税收优惠状态
     *
     * @param queryAbolishDTO 条件
     * @return
     */
    @Override
    public void updateStatus(QueryAbolishDTO queryAbolishDTO) {
        // 根据税收优惠的id查询出税收优惠的列表
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        // 判断废止状态--全文废止
        if (ValidityEnum.FULL_TEXT_REPEAL
                .getValue()
                .equals(queryAbolishDTO.getValidity().name())) {
            // 调用查询废止接口
            List<TaxPreferenceAbolishVO> taxPreferenceAbolish =
                    getTaxPreferenceAbolish(queryAbolishDTO.getId());

            if (taxPreferenceAbolish.size() > 0) {
                for (TaxPreferenceAbolishVO preferenceAbolish : taxPreferenceAbolish) {
                    // 属性拷贝
                    BeanUtils.copyProperties(preferenceAbolish, taxPreferenceDO);
                    // 设置税收优惠状态--失效
                    taxPreferenceDO.setValidity(PreferenceValidation.INVALID);
                }
                // 判断废止状态--部分废止
            }
        } else if (ValidityEnum.PARTIAL_REPEAL.getValue()
                .equals(queryAbolishDTO.getValidity().name())) {
            List<Long> ids = queryAbolishDTO.getIds();

            if (ids != null && ids.size() > 0) {
                // 遍历选中的id集合
                for (Long id : ids) {
                    // 根据id查询税收优惠对象
                    taxPreferenceDO = taxPreferenceMapper.selectById(id);
                    // 设置税收优惠状态--失效
                    taxPreferenceDO.setValidity(PreferenceValidation.INVALID);
                }
            }
        }

        log.info("taxPreferenceDO:{}", taxPreferenceDO);
        if (taxPreferenceDO.getId() != null) {
            taxPreferenceMapper.updateById(taxPreferenceDO);
        }
    }

    /**
     * 查询税收优惠废止的信息
     *
     * @param policiesId 政策法规id
     * @return
     */
    @Override
    public List<TaxPreferenceAbolishVO> getTaxPreferenceAbolish(Long policiesId) {
        if (policiesId == null) {
            return new ArrayList<>();
        }

        // 根据政策法规id查询税收优惠标题
        List<TaxPreferenceDO> taxPreferenceDOS = taxPreferenceMapper.selectByIdList(policiesId);
        if (taxPreferenceDOS.size() == 1 && taxPreferenceDOS.get(0) == null) {
            return new ArrayList<>();
        }
        List<TaxPreferenceAbolishVO> TaxPreferenceAbolishVOList = new ArrayList<>();
        for (TaxPreferenceDO taxPreferenceDO : taxPreferenceDOS) {
            TaxPreferenceAbolishVO taxPreferenceAbolishVO = new TaxPreferenceAbolishVO();
            // 属性拷贝
            BeanUtils.copyProperties(taxPreferenceDO, taxPreferenceAbolishVO);
            TaxPreferenceAbolishVOList.add(taxPreferenceAbolishVO);
        }
        return TaxPreferenceAbolishVOList;
    }

    /**
     * 删除税收优惠关联表
     *
     * @param id 政策法规id
     */
    @Override
    public void deleteTaxPreferencePolicies(Long id) {
        LambdaQueryWrapper<TaxPreferencePoliciesDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(
                !org.springframework.util.StringUtils.isEmpty(id),
                TaxPreferencePoliciesDO::getPoliciesId,
                id);
        taxPreferencePoliciesMapper.delete(queryWrapper);
    }

    /**
     * 根据政策法规id查询关联的税收优惠
     *
     * @param policiesId 政策法规id
     * @return
     */
    @Override
    public List<TaxPreferenceCountVO> getTaxPreferenceId(Long policiesId) {
        List<TaxPreferenceCountVO> taxPreferenceCountVOS =
                taxPreferenceMapper.selectTaxPreferenceId(policiesId);
        return taxPreferenceCountVOS;
    }
}

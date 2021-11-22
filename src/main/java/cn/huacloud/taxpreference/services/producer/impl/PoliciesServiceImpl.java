package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.taxpreference.SortType;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.*;
import cn.huacloud.taxpreference.services.producer.entity.enums.CheckStatus;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import cn.huacloud.taxpreference.services.producer.entity.vos.*;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
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

  private final AttachmentService attachmentService;

  /**
   * 政策列表查询
   *
   * @param queryPoliciesDTO 查询条件
   * @return 返回结果
   */
  @Override
  public PageVO<PoliciesVO> getPolicesList(QueryPoliciesDTO queryPoliciesDTO) {
    queryPoliciesDTO.paramReasonable();
    Page<PoliciesVO> page =
        new Page<>(queryPoliciesDTO.getPageNum(), queryPoliciesDTO.getPageSize());
    // 获取排序字段
    String sort = getSort(queryPoliciesDTO);
    // 列表查询
    IPage<PoliciesVO> policiesDoPage =
        policiesMapper.queryPoliciesVOList(page, queryPoliciesDTO, sort);
    log.info("政策法规列表查询对象={}", policiesDoPage);
    // 返回结果
    return PageVO.createPageVO(policiesDoPage, policiesDoPage.getRecords());
  }

  /** 获取排序字段 */
  private String getSort(QueryPoliciesDTO queryPoliciesDTO) {
    String sort = PoliciesSortType.RELEASE_DATE.getValue();
    // 判断是否为更新时间
    if (PoliciesSortType.UPDATE_TIME.equals(queryPoliciesDTO.getPoliciesSortType())) {
      sort = SortType.UPDATE_TIME.name();
    }
    log.info("排序字段sort:{}", sort);
    return sort;
  }

  /**
   * 新增政策法规
   *
   * @param policiesCombinationDTO 政策法规组合
   * @param userId 用户id
   */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void savePolicies(PoliciesCombinationDTO policiesCombinationDTO, Long userId) {
    log.info("新增政策法规组合dto={}", policiesCombinationDTO);
    // 校验标题和文号是否存在
    judgeExists(policiesCombinationDTO);
    // 新增政策法规
    PoliciesDO policiesDO = new PoliciesDO();
    BeanUtils.copyProperties(policiesCombinationDTO, policiesDO);
    // 填充属性值
    fillProperties(policiesCombinationDTO, userId, policiesDO);
    policiesMapper.insert(policiesDO);
    // 新增后回显id--// TODO: 2021/11/15a
    policiesCombinationDTO.setId(policiesDO.getId());
    PoliciesExplainDTO policiesExplainDtoS = policiesCombinationDTO.getPoliciesExplainDTO();

    // 新增政策解读
    PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
    // 判断当前政策解读对象不存在
    if (policiesExplainDtoS != null) {
      BeanUtils.copyProperties(policiesExplainDtoS, policiesExplainDTO);
      policiesExplainDTO.setPoliciesId(policiesDO.getId());
      policiesExplainService.insertPoliciesExplain(policiesExplainDTO, userId);
    }

    // 新增热点问答
    List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOList =
        policiesCombinationDTO.getFrequentlyAskedQuestionDTOList();
    // 判断该政策法规是否为空
    if (frequentlyAskedQuestionDTOList != null && frequentlyAskedQuestionDTOList.size() > 0) {
      for (FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO : frequentlyAskedQuestionDTOList) {
        // 设置政策法规id
        Long policiesId = policiesDO.getId();
        frequentlyAskedQuestionDTO.setPoliciesIds(String.valueOf(policiesId));
        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(
            frequentlyAskedQuestionDTO, userId);
      }
    }

    // 关联附件信息
    attachmentService.setAttachmentDocId(
        policiesDO.getId(), AttachmentType.POLICIES, policiesCombinationDTO.getContent());
  }

  /**
   * 填充属性值
   *
   * @param policiesCombinationDTO 政策组合对象
   * @param userId 用户id
   * @param policiesDO 政策法规对象
   */
  private void fillProperties(
      PoliciesCombinationDTO policiesCombinationDTO, Long userId, PoliciesDO policiesDO) {
    // 设置纳税人、使用企业、适用行业码值
    policiesDO.setTaxpayerIdentifyTypeCodes(
        StringUtils.join(policiesCombinationDTO.getTaxpayerIdentifyTypeCodes(), ","));
    policiesDO.setEnterpriseTypeCodes(
        StringUtils.join(policiesCombinationDTO.getEnterpriseTypeCodes(), ","));
    policiesDO.setIndustryCodes(StringUtils.join(policiesCombinationDTO.getIndustryCodes(), ","));
    // 进行切分
    String taxpayerIdentifyTypeNames =
        convert2String(policiesCombinationDTO.getTaxpayerIdentifyTypeCodes());
    String enterpriseTypeCodes = convert2String(policiesCombinationDTO.getEnterpriseTypeCodes());
    String industryNames = convert2String(policiesCombinationDTO.getIndustryCodes());

    // 设置纳税人、使用企业、适用行业名称值
    policiesDO.setTaxpayerIdentifyTypeNames(taxpayerIdentifyTypeNames);
    policiesDO.setEnterpriseTypeNames(enterpriseTypeCodes);
    policiesDO.setIndustryNames(industryNames);
    // 设置用户id
    policiesDO.setInputUserId(userId);
    // 添加废止状态
    policiesDO.setPoliciesStatus(PoliciesStatusEnum.PUBLISHED);
    // 添加发布时间
    policiesDO.setReleaseDate(policiesCombinationDTO.getReleaseDate());
    // 添加创建时间
    policiesDO.setCreateTime(LocalDateTime.now());
    // 添加更新时间
    policiesDO.setUpdateTime(LocalDateTime.now());
    // 设置删除
    policiesDO.setDeleted(false);
    // 设置所属区域名称
    policiesDO.setAreaName(
        sysCodeService.getCodeNameByCodeValue(policiesCombinationDTO.getAreaCode()));
    // 设置所属税种名称
    policiesDO.setTaxCategoriesName(
        sysCodeService.getCodeNameByCodeValue(policiesCombinationDTO.getTaxCategoriesCode()));
    // 设置标签
    policiesDO.setLabels(StringUtils.join(policiesCombinationDTO.getLabels(), ","));

    log.info("新增政策法规对象={}", policiesDO);
  }

  /**
   * 校验标题和文号是否重复
   *
   * @param policiesCombinationDTO 政策法规组合
   * @return 返回结果
   */
  private Boolean judgeExists(PoliciesCombinationDTO policiesCombinationDTO) {
    log.info("政策法规组合dto={}", policiesCombinationDTO);
    // 查询标题或文号
    LambdaQueryWrapper<PoliciesDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper
        .eq(
            StringUtils.isNotBlank(policiesCombinationDTO.getTitle()),
            PoliciesDO::getTitle,
            policiesCombinationDTO.getTitle())
        .or()
        .eq(
            StringUtils.isNotBlank(policiesCombinationDTO.getDocCode()),
            PoliciesDO::getDocCode,
            policiesCombinationDTO.getDocCode());
    List<PoliciesDO> policiesDOList = policiesMapper.selectList(lambdaQueryWrapper);
    // 判断是否重复
    if (policiesDOList.size() > 1) {
      throw BizCode._4305.exception();
    }
    // 判断修改的条件
    if (policiesDOList.size() == 1
        && !policiesDOList.get(0).getId().equals(policiesCombinationDTO.getId())) {
      throw BizCode._4305.exception();
    }
    return false;
  }

  /** 拼接转换 */
  @NotNull
  private String convert2String(List<String> industryCodes) {
    Set<String> keySet;
    keySet = new HashSet<>();
    industryCodes.forEach(
        industryCode -> keySet.add(sysCodeService.getCodeNameByCodeValue(industryCode)));
    log.info("keySet={}", keySet);
    return org.apache.commons.lang3.StringUtils.join(keySet, ",");
  }

  /**
   * 根据政策法规id获取详细信息
   *
   * @param id 政策法规id
   * @return PoliciesCombinationDTO
   */
  @Override
  public PoliciesCombinationDTO getPoliciesById(Long id) {
    PoliciesDO policiesDO = policiesMapper.selectById(id);

    // 参数校验
    if (policiesDO == null) {
      throw BizCode._4100.exception();
    }
    // 根据政策法规id查询政策解读对象
    PoliciesExplainDTO policiesExplainDTO =
        policiesExplainService.getPoliciesByPoliciesId(policiesDO.getId());
    // 根据政策法规id查询热门问答
    List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDOList =
        frequentlyAskedQuestionService.getFrequentlyAskedQuestionByPoliciesId(policiesDO.getId());
    // 返回结果
    return setPoliciesCombinationDTO(policiesDO, policiesExplainDTO, frequentlyAskedQuestionDOList);
  }

  /**
   * 设置政策组合对象属性值
   *
   * @param policiesDO 政策法规对象
   * @param policiesExplainDTO 政策解读对象
   * @param frequentlyAskedQuestionDOList 热门问答
   * @return 返回
   */
  @NotNull
  private PoliciesCombinationDTO setPoliciesCombinationDTO(
      PoliciesDO policiesDO,
      PoliciesExplainDTO policiesExplainDTO,
      List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDOList) {
    PoliciesCombinationDTO policiesCombinationDTO = new PoliciesCombinationDTO();
    List<String> strings = new ArrayList<>();
    strings.add("");
    // 设置纳税人、使用企业、适用行业名称值
    if (StringUtils.isNotEmpty(policiesDO.getEnterpriseTypeCodes())) {
      policiesCombinationDTO.setEnterpriseTypeCodes(
          Arrays.asList(policiesDO.getEnterpriseTypeCodes().split(",")));
    } else {
      policiesCombinationDTO.setEnterpriseTypeCodes(strings);
    }
    if (StringUtils.isNotEmpty(policiesDO.getIndustryCodes())) {
      policiesCombinationDTO.setIndustryCodes(
          Arrays.asList(policiesDO.getIndustryCodes().split(",")));
    } else {
      policiesCombinationDTO.setIndustryCodes(strings);
    }
    if (StringUtils.isNotEmpty(policiesDO.getTaxpayerIdentifyTypeCodes())) {
      policiesCombinationDTO.setTaxpayerIdentifyTypeCodes(
          Arrays.asList((policiesDO.getTaxpayerIdentifyTypeCodes()).split(",")));
    } else {
      policiesCombinationDTO.setTaxpayerIdentifyTypeCodes(strings);
    }
    // 设置标签
    if (StringUtils.isNotEmpty(policiesDO.getLabels())) {
      policiesCombinationDTO.setLabels(Arrays.asList(policiesDO.getLabels().split(",")));
    }
    BeanUtils.copyProperties(policiesDO, policiesCombinationDTO);
    // 设置政策解读对象
    policiesCombinationDTO.setPoliciesExplainDTO(policiesExplainDTO);
    // 设置热门问答对象
    policiesCombinationDTO.setFrequentlyAskedQuestionDTOList(frequentlyAskedQuestionDOList);
    return policiesCombinationDTO;
  }

  /**
   * 修改政策法规
   *
   * @param policiesCombinationDTO 政策法规组合
   */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void updatePolicies(PoliciesCombinationDTO policiesCombinationDTO) {
    // 判断标题和文号是否重复
    judgeExists(policiesCombinationDTO);
    // 修改政策法规
    PoliciesDO policiesDO = policiesMapper.selectById(policiesCombinationDTO.getId());
    // 参数校验
    if (policiesDO == null) {
      throw BizCode._4100.exception();
    }
    // 修改政策法规
    updatePolicies(policiesCombinationDTO, policiesDO);
    // 修改政策解读
    insertOrUpdateExplain(policiesCombinationDTO);
    // 热门问答
    insertOrUpdateQA(policiesCombinationDTO);
    // 关联附件信息
    attachmentService.setAttachmentDocId(
        policiesDO.getId(), AttachmentType.POLICIES, policiesCombinationDTO.getContent());
  }

  /**
   * 修改政策法规
   *
   * @param policiesCombinationDTO 政策组合对象
   * @param policiesDO 政策法规对象
   */
  private void updatePolicies(
      PoliciesCombinationDTO policiesCombinationDTO, PoliciesDO policiesDO) {
    BeanUtils.copyProperties(policiesCombinationDTO, policiesDO);
    // 设置纳税人、使用企业、适用行业码值
    policiesDO.setTaxpayerIdentifyTypeCodes(
        StringUtils.join(policiesCombinationDTO.getTaxpayerIdentifyTypeCodes(), ","));
    policiesDO.setEnterpriseTypeCodes(
        StringUtils.join(policiesCombinationDTO.getEnterpriseTypeCodes(), ","));
    policiesDO.setIndustryCodes(StringUtils.join(policiesCombinationDTO.getIndustryCodes(), ","));
    // 进行切分
    String taxpayerIdentifyTypeNames =
        convert2String(policiesCombinationDTO.getTaxpayerIdentifyTypeCodes());
    String enterpriseTypeCodes = convert2String(policiesCombinationDTO.getEnterpriseTypeCodes());
    String industryNames = convert2String(policiesCombinationDTO.getIndustryCodes());
    // 设置纳税人、使用企业、适用行业名称值
    policiesDO.setTaxpayerIdentifyTypeNames(taxpayerIdentifyTypeNames);
    policiesDO.setEnterpriseTypeNames(enterpriseTypeCodes);
    policiesDO.setIndustryNames(industryNames);
    // 设置所属税种
    policiesDO.setTaxCategoriesCode(policiesCombinationDTO.getTaxCategoriesCode());
    policiesDO.setTaxCategoriesName(
        sysCodeService.getCodeNameByCodeValue(policiesCombinationDTO.getTaxCategoriesCode()));
    // 设置区域
    policiesDO.setAreaName(
        sysCodeService.getCodeNameByCodeValue(policiesCombinationDTO.getAreaCode()));
    policiesDO.setAreaCode(policiesCombinationDTO.getAreaCode());
    // 设置标签
    policiesDO.setLabels(StringUtils.join(policiesCombinationDTO.getLabels(), ","));
    // 设置更新时间
    policiesDO.setUpdateTime(LocalDateTime.now());
    // 设置有效性
    policiesDO.setValidity(policiesCombinationDTO.getValidity());
    log.info("修改政策法规对象={}", policiesDO);
    policiesMapper.updateById(policiesDO);
  }

  /**
   * 新增或修改政策解读
   *
   * @param policiesCombinationDTO 政策组合对象
   */
  private void insertOrUpdateExplain(PoliciesCombinationDTO policiesCombinationDTO) {
    PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();

    if (policiesCombinationDTO.getPoliciesExplainDTO() != null) {
      // 获取政策解读id
      Long policiesExplainId = policiesCombinationDTO.getPoliciesExplainDTO().getId();
      BeanUtils.copyProperties(policiesCombinationDTO.getPoliciesExplainDTO(), policiesExplainDTO);
      // 设置政策解读中政策法规的id
      policiesCombinationDTO.getPoliciesExplainDTO().setPoliciesId(policiesCombinationDTO.getId());
      // 根据政策解读id判断是新增或修改
      if (policiesExplainId == null) {
        policiesExplainService.insertPoliciesExplain(
            policiesCombinationDTO.getPoliciesExplainDTO(),
            policiesCombinationDTO.getInputUserId());
      } else {
        policiesExplainService.updatePolicesExplain(policiesExplainDTO);
      }
    }
  }

  /**
   * 新增或修改热门问答
   *
   * @param policiesCombinationDTO 政策组合对象
   */
  private void insertOrUpdateQA(PoliciesCombinationDTO policiesCombinationDTO) {
    // 根据政策法规id查询热门问答集合
    List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionByPoliciesIdList =
        frequentlyAskedQuestionService.getFrequentlyAskedQuestionByPoliciesId(
            policiesCombinationDTO.getId());
    // 差集
    frequentlyAskedQuestionByPoliciesIdList.removeAll(
        policiesCombinationDTO.getFrequentlyAskedQuestionDTOList());
    // 删除
    for (FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO :
        frequentlyAskedQuestionByPoliciesIdList) {
      frequentlyAskedQuestionService.deleteFrequentlyAskedQuestion(
          frequentlyAskedQuestionDTO.getId());
    }
    for (FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO :
        policiesCombinationDTO.getFrequentlyAskedQuestionDTOList()) {
      if (frequentlyAskedQuestionDTO.getId() != null && frequentlyAskedQuestionDTO.getId() != 0) {
        // 修改热门问答
        frequentlyAskedQuestionDTO.setPoliciesIds(String.valueOf(policiesCombinationDTO.getId()));
        frequentlyAskedQuestionDTO.setInputUserId(policiesCombinationDTO.getInputUserId());
        frequentlyAskedQuestionService.updateFrequentlyAskedQuestion(frequentlyAskedQuestionDTO);
      } else {
        // 新增热点问答
        frequentlyAskedQuestionDTO.setPoliciesIds(String.valueOf(policiesCombinationDTO.getId()));
        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(
            frequentlyAskedQuestionDTO, policiesCombinationDTO.getInputUserId());
      }
    }
  }

  /**
   * 校验删除政策法规
   *
   * @param id 政策法规id
   */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public PoliciesCheckDeleteVO checkDeletePoliciesById(Long id) {
    PoliciesDO policiesDO = policiesMapper.selectById(id);
    // 参数校验
    if (policiesDO == null) {
      throw BizCode._4100.exception();
    }
    policiesDO.setDeleted(true);
    log.info("删除政策法规对象={}", policiesDO);

    // 根据政策法规id在关联表中查询所有税收优惠的信息
    List<TaxPreferenceCountVO> taxPreferenceCountVOS =
        taxPreferenceService.getTaxPreferenceId(policiesDO.getId());
    // 判断当前政策法规id是否和税收优惠先关联
    if (taxPreferenceCountVOS.isEmpty()) {
      return new PoliciesCheckDeleteVO().setCheckStatus(PoliciesCheckDeleteVO.CheckStatus.OK);
    }

    Map<CheckStatus, List<TaxPreferenceCountVO>> map = new LinkedHashMap<>();
    for (TaxPreferenceCountVO taxPreferenceCountVO : taxPreferenceCountVOS) {
      // 获取税收优惠的数量
      long count = taxPreferenceCountVO.getCount();
      // 判断当前关联表中税收优惠的数量--大于一，表示一个政策法规关联了多个优惠事项
      if (count > 1) {
        List<TaxPreferenceCountVO> list =
            map.computeIfAbsent(CheckStatus.INFO, key -> new ArrayList<>());
        list.add(taxPreferenceCountVO);
        // 判断当前关联表中税收优惠的数量--等于一，表示一个政策法规关联了一个优惠事项
      } else if (count == 1) {
        List<TaxPreferenceCountVO> list =
            map.computeIfAbsent(CheckStatus.CAN_NOT, key -> new ArrayList<>());
        list.add(taxPreferenceCountVO);
      }
    }
    // 判断是否包含了该状态值
    if (map.containsKey(CheckStatus.CAN_NOT)) {
      return new PoliciesCheckDeleteVO()
          .setCheckStatus(PoliciesCheckDeleteVO.CheckStatus.CAN_NOT)
          .setCheckList(map.get(CheckStatus.CAN_NOT));
    }

    if (map.containsKey(CheckStatus.INFO)) {
      return new PoliciesCheckDeleteVO()
          .setCheckStatus(PoliciesCheckDeleteVO.CheckStatus.INFO)
          .setCheckList(map.get(CheckStatus.INFO));
    }
    return null;
  }

  /**
   * 删除政策法规
   *
   * @param id 政策法规id
   */
  @Override
  public void confirmDeletePoliciesById(Long id) {
    PoliciesDO policiesDO = policiesMapper.selectById(id);
    // 参数校验
    if (policiesDO == null) {
      throw BizCode._4100.exception();
    }
    policiesDO.setDeleted(true);
    log.info("删除政策法规对象={}", policiesDO);
    // 删除税收优惠,根据政策法规id查询税收优惠id--关联表中
    List<TaxPreferenceCountVO> taxPreferenceCounts =
        taxPreferenceService.getTaxPreferenceId(policiesDO.getId());

    for (TaxPreferenceCountVO taxPreferenceCountVO : taxPreferenceCounts) {
      Long count = taxPreferenceCountVO.getCount();
      if (count > 1) {
        // 删除政策法规
        policiesMapper.updateById(policiesDO);
        // 删除政策解读
        deletePoliciesExplain(policiesDO);
        // 删除热点问答
        deleteFrequentlyAskedQuestion(policiesDO);
        taxPreferenceService.deleteTaxPreferencePolicies(policiesDO.getId());
      }
      if (count == 1) {
        // 判断查询结果是单个，提示无法删除
        throw BizCode._4306.exception();
      }
      if (count < 1) {
        // 删除政策法规
        policiesMapper.updateById(policiesDO);
        // 删除政策解读
        deletePoliciesExplain(policiesDO);
        // 删除热点问答
        deleteFrequentlyAskedQuestion(policiesDO);
      }
    }
    if (taxPreferenceCounts.isEmpty()) {
      // 删除政策法规
      policiesMapper.updateById(policiesDO);
      // 删除政策解读
      deletePoliciesExplain(policiesDO);
      // 删除热点问答
      deleteFrequentlyAskedQuestion(policiesDO);
    }
  }

  /**
   * 删除热门问答
   *
   * @param policiesDO 政策法规对象
   */
  private void deleteFrequentlyAskedQuestion(PoliciesDO policiesDO) {
    // 根据政策法规id查询热门问答
    List<FrequentlyAskedQuestionDO> frequentlyAskedQuestionIds =
        policiesMapper.selectFrequentlyAskedQuestionId(policiesDO.getId());
    log.info("热点问答id集合={}", frequentlyAskedQuestionIds);
    // 遍历删除政策法规关联关系
    for (FrequentlyAskedQuestionDO frequentlyAskedQuestionId : frequentlyAskedQuestionIds) {
      List<String> ids = Arrays.asList(frequentlyAskedQuestionId.getPoliciesIds().split(","));
      ArrayList<String> list = new ArrayList<>(ids);
      list.remove(String.valueOf(policiesDO.getId()));
      frequentlyAskedQuestionId.setPoliciesIds(StringUtils.join(list, ","));
      frequentlyAskedQuestionMapper.updateById(frequentlyAskedQuestionId);
    }
  }

  /**
   * 删除政策解读
   *
   * @param policiesDO 政策法规对象
   */
  private void deletePoliciesExplain(PoliciesDO policiesDO) {
    // 根据政策法规查询关联的政策解读id
    Long policiesExplainId = policiesMapper.selectExplainId(policiesDO.getId());
    // 根据id删除政策解读
    if (policiesExplainId != null) {
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
    // 参数校验
    if (queryAbolishDTO == null || queryAbolishDTO.getAbolishNote() == null) {
      throw BizCode._4308.exception();
    }
    // 查询政策法规
    Long id = queryAbolishDTO.getId();
    PoliciesDO policiesDO = policiesMapper.selectById(id);
    // 参数校验
    if (policiesDO == null) {
      throw BizCode._4100.exception();
    }
    // 判断条件--全文废止
    if (ValidityEnum.FULL_TEXT_REPEAL.getValue().equals(queryAbolishDTO.getValidity())) {
      // 设置政策法规的有效性
      policiesDO.setValidity(ValidityEnum.FULL_TEXT_REPEAL);
      policiesDO.setAbolishNote(queryAbolishDTO.getAbolishNote());
      // 判断条件--部分废止
    } else if (ValidityEnum.PARTIAL_REPEAL.getValue().equals(queryAbolishDTO.getValidity())) {
      // 设置政策法规的有效性
      policiesDO.setValidity(ValidityEnum.PARTIAL_VALID);
      policiesDO.setAbolishNote(queryAbolishDTO.getAbolishNote());
    }
    // 更新税收优惠的状态
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
    // 查询政策法规
    PoliciesDO policiesDO = policiesMapper.selectById(id);
    List<TaxPreferenceAbolishVO> taxPreferenceAbolish =
        taxPreferenceService.getTaxPreferenceAbolish(id);
    // 设置返回结果值
    PoliciesAbolishVO policiesAbolishVO = new PoliciesAbolishVO();
    // 设置政策法规状态--todo
    policiesAbolishVO.setValidityEnum(policiesDO.getValidity());
    // 设置废止信息
    policiesAbolishVO.setAbolishNote(policiesDO.getAbolishNote());
    // 设置税收优惠名称
    policiesAbolishVO.setTaxPreferenceVOS(taxPreferenceAbolish);
    // 返回结果
    log.info("查询政策法规废止的对象={}", policiesAbolishVO);
    return policiesAbolishVO;
  }

  @Override
  public Boolean checkTitleAndDocCode(String titleOrDocCode) {
    log.info("标题或文号={}", titleOrDocCode);
    // 查询标题和文号
    LambdaQueryWrapper<PoliciesDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper
        .eq(StringUtils.isNotBlank(titleOrDocCode), PoliciesDO::getTitle, titleOrDocCode)
        .or()
        .eq(StringUtils.isNotBlank(titleOrDocCode), PoliciesDO::getDocCode, titleOrDocCode);
    List<PoliciesDO> policiesDOS = policiesMapper.selectList(lambdaQueryWrapper);
    // 判断是否重复
    if (policiesDOS.size() > 1 || policiesDOS.size() == 1) {
      return true;
    }
    return false;
  }
}

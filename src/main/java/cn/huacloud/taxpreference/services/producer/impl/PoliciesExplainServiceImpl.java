package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesExplainStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 政策解读服务实现类
 *
 * @author wuxin
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PoliciesExplainServiceImpl implements PoliciesExplainService {

  private final PoliciesExplainMapper policiesExplainMapper;

  /**
   * 政策解读列表
   *
   * @param queryPoliciesExplainDTO 政策解读查询对象
   * @return 返回
   */
  @Override
  public PageVO<PoliciesExplainDetailVO> getPoliciesExplainList(
      QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
    log.info("政策解读列表查询条件dto", queryPoliciesExplainDTO);
    LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    // 模糊查询--政策解读标题
    lambdaQueryWrapper.like(
        StringUtils.isNotBlank(queryPoliciesExplainDTO.getKeyword()),
        PoliciesExplainDO::getTitle,
        queryPoliciesExplainDTO.getKeyword());
    // 模糊查询--政策解读标题
    lambdaQueryWrapper.like(
        StringUtils.isNotBlank(queryPoliciesExplainDTO.getTitle()),
        PoliciesExplainDO::getTitle,
        queryPoliciesExplainDTO.getTitle());
    // 模糊查询--政策解读来源
    lambdaQueryWrapper.like(
        StringUtils.isNotBlank(queryPoliciesExplainDTO.getDocSource()),
        PoliciesExplainDO::getDocSource,
        queryPoliciesExplainDTO.getDocSource());
    // 条件查询--发布日期
    lambdaQueryWrapper
        .ge(
            queryPoliciesExplainDTO.getStartTime() != null,
            PoliciesExplainDO::getReleaseDate,
            queryPoliciesExplainDTO.getStartTime())
        .le(
            queryPoliciesExplainDTO.getEndTime() != null,
            PoliciesExplainDO::getReleaseDate,
            queryPoliciesExplainDTO.getEndTime());
    lambdaQueryWrapper.eq(PoliciesExplainDO::getDeleted, false);

    // 排序--发布时间
    if (PoliciesSortType.RELEASE_DATE.equals(queryPoliciesExplainDTO.getSortField())) {
      lambdaQueryWrapper
          .eq(
              queryPoliciesExplainDTO.getReleaseDate() != null,
              PoliciesExplainDO::getReleaseDate,
              queryPoliciesExplainDTO.getReleaseDate())
          .orderByDesc(PoliciesExplainDO::getReleaseDate);
    }
    // 排序--更新时间
    if (PoliciesSortType.UPDATE_TIME.equals(queryPoliciesExplainDTO.getSortField())) {
      lambdaQueryWrapper
          .eq(
              queryPoliciesExplainDTO.getUpdateTime() != null,
              PoliciesExplainDO::getUpdateTime,
              queryPoliciesExplainDTO.getUpdateTime())
          .orderByDesc(PoliciesExplainDO::getUpdateTime);
    }
    // 分页
    IPage<PoliciesExplainDO> policiesExplainDoPage =
        policiesExplainMapper.selectPage(
            new Page<>(queryPoliciesExplainDTO.getPageNum(), queryPoliciesExplainDTO.getPageSize()),
            lambdaQueryWrapper);
    // 数据映射
    List<PoliciesExplainDetailVO> records =
        policiesExplainDoPage.getRecords().stream()
            .map(
                policiesExplainDO -> {
                  PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
                  // 属性拷贝
                  BeanUtils.copyProperties(policiesExplainDO, policiesExplainDetailVO);
                  return policiesExplainDetailVO;
                })
            .collect(Collectors.toList());
    log.info("政策解读列表查询对象", policiesExplainDoPage);
    return PageVO.createPageVO(policiesExplainDoPage, records);
  }

  /**
   * 新增政策解读
   *
   * @param policiesExplainDTO 政策解读对象
   * @param userId 用户id
   */
  @Override
  public void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long userId) {
    log.info("新增政策解读dto={}", policiesExplainDTO);
    PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
    // 属性拷贝
    BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
    // 设置值
    policiesExplainDO.setPoliciesId(policiesExplainDTO.getPoliciesId());
    // 设置用户id
    policiesExplainDO.setInputUserId(userId);
    // 设置发布时间
    policiesExplainDO.setReleaseDate(LocalDate.now());
    // 设置创建时间
    policiesExplainDO.setCreateTime(LocalDateTime.now());
    // 设置更新时间
    policiesExplainDO.setUpdateTime(LocalDateTime.now());
    // 设置逻辑删除
    policiesExplainDO.setDeleted(false);
    // 设置政策解读状态
    policiesExplainDO.setPoliciesExplainStatus(PoliciesExplainStatusEnum.PUBLISHED);
    log.info("新增政策解读对象={}", policiesExplainDO);
    policiesExplainMapper.insert(policiesExplainDO);
  }

  /**
   * 修改政策解读
   *
   * @param policiesExplainDTO 政策解读对象
   */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void updatePolicesExplain(PoliciesExplainDTO policiesExplainDTO) {
    // 查询政策解读
    PoliciesExplainDO policiesExplainDO =
        policiesExplainMapper.selectById(policiesExplainDTO.getId());
    // 参数校验
    if (policiesExplainDO != null) {
      policiesExplainDO.setUpdateTime(LocalDateTime.now());
      // 属性拷贝
      BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
      log.info("修改政策解读对象={}", policiesExplainDO);
      // 修改政策解读
      policiesExplainMapper.updateById(policiesExplainDO);
    }
  }

  /**
   * 根据政策解读id查询政策解读详情
   *
   * @param id 政策解读id
   * @return 返回
   */
  @Override
  public PoliciesExplainDetailVO getPoliciesById(Long id) {
    // 根据政策解读id查询
    PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectById(id);
    PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
    // 属性拷贝
    BeanUtils.copyProperties(policiesExplainDO, policiesExplainDetailVO);
    log.info("政策解读对象详情={}", policiesExplainDO);
    // 返回结果
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
    log.info("删除政策解读对象={}", policiesExplainDO);
    policiesExplainMapper.updateById(policiesExplainDO);
  }

  /** @return 返回 */
  @Override
  public List<PoliciesTitleVO> fuzzyQuery() {
    // 查询该政策解读是否被关联了政策法规
    List<PoliciesTitleVO> relatedPolicyList = policiesExplainMapper.getRelatedPolicy();
    log.info("查询该政策解读是否被关联了政策法规={}", relatedPolicyList);
    return relatedPolicyList;
  }

  /**
   * 根据政策法规id查询政策解读信息
   *
   * @param policiesId 政策法规id
   * @return 返回
   */
  @Override
  public PoliciesExplainDTO getPoliciesByPoliciesId(Long policiesId) {
    LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    // 根据id和delete查询
    lambdaQueryWrapper.eq(PoliciesExplainDO::getPoliciesId, policiesId);
    lambdaQueryWrapper.eq(PoliciesExplainDO::getDeleted, false);
    PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectOne(lambdaQueryWrapper);

    PoliciesExplainDTO policiesExplainDTO = new PoliciesExplainDTO();
    // 判断是否为空
    if (policiesExplainDO == null) {
      return null;
    }
    BeanUtils.copyProperties(policiesExplainDO, policiesExplainDTO);
    log.info("根据政策法规id查询政策解读信息={}", policiesExplainDTO);
    return policiesExplainDTO;
  }
}

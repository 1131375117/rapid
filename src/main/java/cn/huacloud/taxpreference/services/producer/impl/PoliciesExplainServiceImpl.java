package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.producer.PoliciesExplainService;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesExplainStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainListVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.sync.es.trigger.impl.PoliciesExplainEventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	private final AttachmentService attachmentService;

	private PoliciesService policiesService;

	private final PoliciesExplainEventTrigger policiesExplainEventTrigger;

	@Autowired
	public void setPoliciesService(PoliciesService policiesService) {
		this.policiesService = policiesService;
	}

	/**
	 * 政策解读列表
	 *
	 * @param queryPoliciesExplainDTO 政策解读查询对象
	 * @return 返回
	 */
	@Override
	public PageVO<PoliciesExplainListVO> getPoliciesExplainList(
			QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
		log.info("政策解读列表查询条件dto={}", queryPoliciesExplainDTO);
		String keyword = queryPoliciesExplainDTO.getKeyword();
		String title = queryPoliciesExplainDTO.getTitle();
		String docSource = queryPoliciesExplainDTO.getDocSource();
		LocalDate startTime = queryPoliciesExplainDTO.getStartTime();
		LocalDate endTime = queryPoliciesExplainDTO.getEndTime();
		LocalDate releaseDate = queryPoliciesExplainDTO.getReleaseDate();
		LocalDateTime updateTime = queryPoliciesExplainDTO.getUpdateTime();
		//构建查询条件
		LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		// 关键字查询--政策解读标题和文号
		lambdaQueryWrapper.and(keyword != null,
				k -> k.like(PoliciesExplainDO::getTitle, keyword).or()
						.like(PoliciesExplainDO::getDocSource, keyword));
		// 条件模糊查询--政策解读标题
		lambdaQueryWrapper
				.like(title != null, PoliciesExplainDO::getTitle, title);
		// 条件模糊查询--政策解读来源
		lambdaQueryWrapper
				.like(docSource != null, PoliciesExplainDO::getDocSource, docSource);
		// 条件查询--发布日期
		lambdaQueryWrapper
				.ge(startTime != null, PoliciesExplainDO::getReleaseDate, startTime)
				.le(endTime != null, PoliciesExplainDO::getReleaseDate, endTime);
		lambdaQueryWrapper.eq(PoliciesExplainDO::getDeleted, false);

		// 排序--发布时间
		if (PoliciesSortType.RELEASE_DATE.equals(queryPoliciesExplainDTO.getSortField())) {
			lambdaQueryWrapper
					.eq(releaseDate != null, PoliciesExplainDO::getReleaseDate, releaseDate)
					.orderByDesc(PoliciesExplainDO::getReleaseDate)
					.orderByDesc(PoliciesExplainDO::getUpdateTime);
		}
		// 排序--更新时间
		if (PoliciesSortType.UPDATE_TIME.equals(queryPoliciesExplainDTO.getSortField())) {
			lambdaQueryWrapper
					.eq(updateTime != null, PoliciesExplainDO::getUpdateTime, updateTime)
					.orderByDesc(PoliciesExplainDO::getUpdateTime);
		}
		// 分页
		IPage<PoliciesExplainDO> policiesExplainDoPage =
				policiesExplainMapper.selectPage(
						new Page<>(queryPoliciesExplainDTO.getPageNum(), queryPoliciesExplainDTO.getPageSize()),
						lambdaQueryWrapper);
		// 数据映射
		List<PoliciesExplainListVO> records =
				policiesExplainDoPage.getRecords().stream().map(policiesExplainDO -> {
					PoliciesExplainListVO policiesExplainListVO = new PoliciesExplainListVO();
					// 属性拷贝
					BeanUtils.copyProperties(policiesExplainDO, policiesExplainListVO);
					return policiesExplainListVO;
				}).collect(Collectors.toList());
		log.info("政策解读列表查询对象={}", policiesExplainDoPage);
		return PageVO.createPageVO(policiesExplainDoPage, records);
	}

	/**
	 * 新增政策解读
	 *
	 * @param policiesExplainDTO 政策解读对象
	 * @param userId             用户id
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long userId) {
		log.info("新增政策解读dto={}", policiesExplainDTO);
		if (policiesExplainDTO.getPoliciesId() == null) {
			throw BizCode._4100.exception();
		}
		checkAssociation(policiesExplainDTO);
		PoliciesExplainDO policiesExplainDO = new PoliciesExplainDO();
		// 属性拷贝
		BeanUtils.copyProperties(policiesExplainDTO, policiesExplainDO);
		// 设置值
		policiesExplainDO.setPoliciesId(policiesExplainDTO.getPoliciesId());
		// 设置用户id
		policiesExplainDO.setInputUserId(userId);
		// 设置发布时间
		policiesExplainDO.setReleaseDate(policiesExplainDTO.getReleaseDate());
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
		// 关联附件信息
		attachmentService.setAttachmentDocId(
				policiesExplainDO.getId(), AttachmentType.POLICIES_EXPLAIN, policiesExplainDTO.getContent());
		// 政策解读保存触发事件
		policiesExplainEventTrigger.saveEvent(policiesExplainDO.getId());
	}

	/**
	 * 校验当前政策法规id有没有其他政策解读关联
	 *
	 * @param policiesExplainDTO
	 */
	@Override
	public void checkAssociation(PoliciesExplainDTO policiesExplainDTO) {
		// 校验当前政策法规id有没有其他政策解读关联
		LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(PoliciesExplainDO::getPoliciesId, policiesExplainDTO.getPoliciesId());
		lambdaQueryWrapper.eq(PoliciesExplainDO::getDeleted, false);
		List<PoliciesExplainDO> policiesExplainDoS = policiesExplainMapper.selectList(lambdaQueryWrapper);
		if (policiesExplainDoS.size() > 1) {
			throw BizCode._4308.exception();
		}
		if (policiesExplainDoS.size() == 1 && !policiesExplainDTO.getPoliciesId().equals(policiesExplainDoS.get(0).getPoliciesId())) {
			throw BizCode._4308.exception();
		}
	}

	/**
	 * 修改政策解读
	 *
	 * @param policiesExplainDTO 政策解读对象
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updatePolicesExplain(PoliciesExplainDTO policiesExplainDTO) {
		checkAssociation(policiesExplainDTO);
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
			// 关联附件信息
			attachmentService.setAttachmentDocId(
					policiesExplainDO.getId(), AttachmentType.POLICIES, policiesExplainDTO.getContent());
			// 触发事件
			policiesExplainEventTrigger.saveEvent(policiesExplainDO.getId());
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
		if (policiesExplainDO.getPoliciesId() != null) {
			PoliciesTitleVO policiesTitleVO = new PoliciesTitleVO();
			PoliciesDO policies = policiesService.getPolicies(policiesExplainDO.getPoliciesId());
			BeanUtils.copyProperties(policies, policiesTitleVO);
			policiesExplainDetailVO.setPolicies(policiesTitleVO);
		}
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
		// 触发事件
		policiesExplainEventTrigger.deleteEvent(id);
	}

	@Override
	public void deletePoliciesByPolicies(Long policiesId) {
		LambdaQueryWrapper<PoliciesExplainDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(PoliciesExplainDO::getPoliciesId, policiesId);
		lambdaQueryWrapper.eq(PoliciesExplainDO::getDeleted, false);
		PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectOne(lambdaQueryWrapper);
		policiesExplainDO.setDeleted(true);
		policiesExplainMapper.updateById(policiesExplainDO);
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

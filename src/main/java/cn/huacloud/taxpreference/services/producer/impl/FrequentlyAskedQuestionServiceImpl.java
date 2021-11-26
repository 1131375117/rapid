package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
import cn.huacloud.taxpreference.sync.es.trigger.impl.FAQEventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 热门问答服务实现类
 *
 * @author wuxin
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FrequentlyAskedQuestionServiceImpl implements FrequentlyAskedQuestionService {

	private final FrequentlyAskedQuestionMapper frequentlyAskedQuestionMapper;

	private final AttachmentService attachmentService;

	private PoliciesService policiesService;

	private final FAQEventTrigger faqEventTrigger;

	@Autowired
	public void setPoliciesService(PoliciesService policiesService) {
		this.policiesService = policiesService;
	}

	/**
	 * 热门问答列表查询
	 *
	 * @param queryPoliciesExplainDTO 查询条件
	 * @return 返回
	 */
	@Override
	public PageVO<PoliciesExplainDetailVO> getFrequentlyAskedQuestionList(
			QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
		log.info("热门问答查询列表条件dto={}", queryPoliciesExplainDTO);
		String keyword = queryPoliciesExplainDTO.getKeyword();
		String title = queryPoliciesExplainDTO.getTitle();
		String docSource = queryPoliciesExplainDTO.getDocSource();
		LocalDate startTime = queryPoliciesExplainDTO.getStartTime();
		LocalDate endTime = queryPoliciesExplainDTO.getEndTime();
		LocalDate releaseDate = queryPoliciesExplainDTO.getReleaseDate();
		LocalDateTime updateTime = queryPoliciesExplainDTO.getUpdateTime();
		//构建查询条件
		LambdaQueryWrapper<FrequentlyAskedQuestionDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper
				.and(keyword != null, k -> k.like(FrequentlyAskedQuestionDO::getTitle, keyword).or()
						.like(FrequentlyAskedQuestionDO::getDocSource, keyword));
		// 模糊查询--政策解读标题
		lambdaQueryWrapper.like(title != null, FrequentlyAskedQuestionDO::getTitle, title);
		// 模糊查询--政策解读来源
		lambdaQueryWrapper.like(docSource != null, FrequentlyAskedQuestionDO::getDocSource, docSource);
		// 条件查询--发布日期
		lambdaQueryWrapper
				.ge(startTime != null, FrequentlyAskedQuestionDO::getReleaseDate, startTime)
				.le(endTime != null, FrequentlyAskedQuestionDO::getReleaseDate, endTime);
		lambdaQueryWrapper.eq(FrequentlyAskedQuestionDO::getDeleted, false);
		// 排序--发布时间
		if (PoliciesSortType.RELEASE_DATE.equals(queryPoliciesExplainDTO.getSortField())) {
			lambdaQueryWrapper
					.eq(releaseDate != null, FrequentlyAskedQuestionDO::getReleaseDate, releaseDate)
					.orderByDesc(FrequentlyAskedQuestionDO::getReleaseDate)
					.orderByDesc(FrequentlyAskedQuestionDO::getUpdateTime);
		}
		// 排序--更新时间
		if (PoliciesSortType.UPDATE_TIME.equals(queryPoliciesExplainDTO.getSortField())) {
			lambdaQueryWrapper
					.eq(updateTime != null, FrequentlyAskedQuestionDO::getUpdateTime, updateTime)
					.orderByDesc(FrequentlyAskedQuestionDO::getUpdateTime);
		}
		// 分页
		IPage<FrequentlyAskedQuestionDO> frequentlyAskedQuestionDoPage =
				frequentlyAskedQuestionMapper.selectPage(
						new Page<>(queryPoliciesExplainDTO.getPageNum(), queryPoliciesExplainDTO.getPageSize()),
						lambdaQueryWrapper);
		// 数据映射
		List<PoliciesExplainDetailVO> records =
				frequentlyAskedQuestionDoPage.getRecords().stream()
						.map(frequentlyAskedQuestionDO -> {
									PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
									// 属性拷贝
									BeanUtils.copyProperties(frequentlyAskedQuestionDO, policiesExplainDetailVO);
									// policiesExplainDetailVO.setPoliciesIds(frequentlyAskedQuestionDO.getPoliciesIds());
									return policiesExplainDetailVO;
								})
						.collect(Collectors.toList());
		log.info("热门问答查询列表对象={}", frequentlyAskedQuestionDoPage);
		return PageVO.createPageVO(frequentlyAskedQuestionDoPage, records);
	}

	/**
	 * 新增热门问答
	 *
	 * @param frequentlyAskedQuestionDto 热门问答对象
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void insertFrequentlyAskedQuestion(
			FrequentlyAskedQuestionDTO frequentlyAskedQuestionDto, Long userId) {
		log.info("新增热门问答dto={}", frequentlyAskedQuestionDto);
		FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = new FrequentlyAskedQuestionDO();
		BeanUtils.copyProperties(frequentlyAskedQuestionDto, frequentlyAskedQuestionDO);
		// 设置发布时间
		frequentlyAskedQuestionDO.setReleaseDate(frequentlyAskedQuestionDto.getReleaseDate());
		// 设置用户id
		frequentlyAskedQuestionDO.setInputUserId(userId);
		// 设置创建时间
		frequentlyAskedQuestionDO.setCreateTime(LocalDateTime.now());
		// 设置更新时间
		frequentlyAskedQuestionDO.setUpdateTime(LocalDateTime.now());
		// 设置逻辑删除
		frequentlyAskedQuestionDO.setDeleted(false);
		frequentlyAskedQuestionDO.setPoliciesIds(frequentlyAskedQuestionDto.getPoliciesIds());
		frequentlyAskedQuestionDO.setId(null);
		log.info("新增热门问答对象={}", frequentlyAskedQuestionDO);
		frequentlyAskedQuestionMapper.insert(frequentlyAskedQuestionDO);
		// 关联附件信息
		attachmentService.setAttachmentDocId(
				frequentlyAskedQuestionDO.getId(),
				AttachmentType.POLICIES,
				frequentlyAskedQuestionDto.getContent());
		// 触发事件
		faqEventTrigger.saveEvent(frequentlyAskedQuestionDO.getId());
	}

	/**
	 * 修改热门问答
	 *
	 * @param frequentlyAskedQuestionDto 热门问答对象
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateFrequentlyAskedQuestion(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDto) {
		// 查询热门问答
		FrequentlyAskedQuestionDO frequentlyAskedQuestionDO =
				frequentlyAskedQuestionMapper.selectById(frequentlyAskedQuestionDto.getId());
		// 参数校验
		if (frequentlyAskedQuestionDO != null) {
			frequentlyAskedQuestionDO.setUpdateTime(LocalDateTime.now());
			// 属性拷贝
			BeanUtils.copyProperties(frequentlyAskedQuestionDto, frequentlyAskedQuestionDO);
			log.info("修改热门问答对象={}", frequentlyAskedQuestionDO);
			// 修改热门问答
			frequentlyAskedQuestionMapper.updateById(frequentlyAskedQuestionDO);
			// 关联附件信息
			attachmentService.setAttachmentDocId(
					frequentlyAskedQuestionDto.getId(),
					AttachmentType.POLICIES,
					frequentlyAskedQuestionDto.getContent());
			// 触发事件
			faqEventTrigger.saveEvent(frequentlyAskedQuestionDO.getId());
		}
	}

	/**
	 * 删除热门问答
	 *
	 * @param id 热门问答id
	 */
	@Override
	public void deleteFrequentlyAskedQuestion(Long id) {
		FrequentlyAskedQuestionDO frequentlyAskedQuestionDO =
				frequentlyAskedQuestionMapper.selectById(id);
		// 参数校验
		if (frequentlyAskedQuestionDO == null) {
			throw BizCode._4100.exception();
		}
		frequentlyAskedQuestionDO.setDeleted(true);
		log.info("删除问答查询列表对象={}", frequentlyAskedQuestionDO);
		frequentlyAskedQuestionMapper.updateById(frequentlyAskedQuestionDO);
		// 触发事件
		faqEventTrigger.deleteEvent(frequentlyAskedQuestionDO.getId());
	}

	/**
	 * 根据政策法规id查询热门问答信息
	 *
	 * @param policiesId 政策法规id
	 * @return 返回
	 */
	@Override
	public List<FrequentlyAskedQuestionDTO> getFrequentlyAskedQuestionByPoliciesId(Long policiesId) {
		LambdaQueryWrapper<FrequentlyAskedQuestionDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		// 转换
		String policiesIds = String.valueOf(policiesId);
		// 根据id和delete查询
		lambdaQueryWrapper.eq(FrequentlyAskedQuestionDO::getPoliciesIds, policiesIds);
		lambdaQueryWrapper.eq(FrequentlyAskedQuestionDO::getDeleted, false);
		List<FrequentlyAskedQuestionDO> frequentlyAskedQuestionDoS =
				frequentlyAskedQuestionMapper.selectList(lambdaQueryWrapper);
		// 判断是否为空
		if (frequentlyAskedQuestionDoS != null) {
			List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionVoList = new ArrayList<>();
			for (FrequentlyAskedQuestionDO frequentlyAskedQuestionDO : frequentlyAskedQuestionDoS) {
				// 属性拷贝
				FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO = new FrequentlyAskedQuestionDTO();
				BeanUtils.copyProperties(frequentlyAskedQuestionDO, frequentlyAskedQuestionDTO);
				frequentlyAskedQuestionVoList.add(frequentlyAskedQuestionDTO);
			}
			return frequentlyAskedQuestionVoList;
		}
		return null;
	}

	/**
	 * 根据热门问答id查询详情
	 *
	 * @param id 热门问答id
	 * @return 返回
	 */
	@Override
	public PoliciesExplainDetailVO getFrequentlyAskedQuestionById(Long id) {
		// 查询热门问答对象
		FrequentlyAskedQuestionDO frequentlyAskedQuestionDO =
				frequentlyAskedQuestionMapper.selectById(id);
		PoliciesExplainDetailVO policiesExplainDetailVO = new PoliciesExplainDetailVO();
		String policiesIds = frequentlyAskedQuestionDO.getPoliciesIds();
		if (policiesIds != null && !"".equals(policiesIds)) {
			Long aLong = Long.valueOf(policiesIds);
			PoliciesDO policies = policiesService.getPolicies(aLong);
			policiesExplainDetailVO.setPoliciesTitle(policies.getTitle());
		}
		// 属性拷贝
		BeanUtils.copyProperties(frequentlyAskedQuestionDO, policiesExplainDetailVO);
		return policiesExplainDetailVO;
	}
}

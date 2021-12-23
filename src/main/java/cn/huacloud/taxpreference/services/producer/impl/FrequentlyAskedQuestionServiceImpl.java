package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.taxpreference.SortType;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import cn.huacloud.taxpreference.services.producer.entity.vos.FrequentlyAskedQuestionDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.FrequentlyAskedQuestionVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

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
	public PageVO<FrequentlyAskedQuestionVO> getFrequentlyAskedQuestionList(
			QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
		log.info("热门问答查询列表条件dto={}", queryPoliciesExplainDTO);

		String sort = getSort(queryPoliciesExplainDTO);
		IPage<FrequentlyAskedQuestionVO> frequentlyAskedQuestionDoPage=frequentlyAskedQuestionMapper.selectPageList(queryPoliciesExplainDTO.createQueryPage(),sort,queryPoliciesExplainDTO);
		log.info("热门问答查询列表对象={}", frequentlyAskedQuestionDoPage);
		return PageVO.createPageVO(frequentlyAskedQuestionDoPage,frequentlyAskedQuestionDoPage.getRecords());
	}

	/**
	 * 获取排序字段
	 */
	private String getSort(QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
		String sort = PoliciesSortType.RELEASE_DATE.getValue();
		// 判断是否为更新时间
		if (PoliciesSortType.UPDATE_TIME.equals(queryPoliciesExplainDTO.getSortField())) {
			sort = SortType.UPDATE_TIME.name();
		}
		log.info("排序字段sort:{}", sort);
		return sort;
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

		frequentlyAskedQuestionDO.setPoliciesIds(StringUtils.join(frequentlyAskedQuestionDto.getPoliciesIds(),","));
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
			BeanUtils.copyProperties(frequentlyAskedQuestionDto, frequentlyAskedQuestionDO);
			frequentlyAskedQuestionDO.setUpdateTime(LocalDateTime.now());
			frequentlyAskedQuestionDO.setPoliciesIds(StringUtils.join(frequentlyAskedQuestionDto.getPoliciesIds(),","));
			// 属性拷贝
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
	@Transactional(rollbackFor = Exception.class)
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
	public FrequentlyAskedQuestionDetailVO getFrequentlyAskedQuestionById(Long id) {
		// 查询热门问答对象
		FrequentlyAskedQuestionDO frequentlyAskedQuestionDO =
				frequentlyAskedQuestionMapper.selectById(id);
		FrequentlyAskedQuestionDetailVO frequentlyAskedQuestionDetailVO = new FrequentlyAskedQuestionDetailVO();
		String policiesIds = frequentlyAskedQuestionDO.getPoliciesIds();
		if (policiesIds != null && !"".equals(policiesIds)) {
			frequentlyAskedQuestionDetailVO.setPoliciesIds(Arrays.asList(frequentlyAskedQuestionDO.getPoliciesIds().split(",")));
			String[] policiesIdsList = frequentlyAskedQuestionDO.getPoliciesIds().split(",");
			List<PoliciesTitleVO> list = new ArrayList<>();
			for (String policiesId : policiesIdsList) {
				PoliciesTitleVO policiesTitleVO = new PoliciesTitleVO();
				if (!isInteger(policiesId)) {
					throw BizCode._4100.exception();
				}
				Long aLong = Long.valueOf(policiesId);
				PoliciesDO policies = policiesService.getPolicies(aLong);
				BeanUtils.copyProperties(policies,policiesTitleVO);
				list.add(policiesTitleVO);
			}
			frequentlyAskedQuestionDetailVO.setPolicies(list);
		}
		// 属性拷贝
		BeanUtils.copyProperties(frequentlyAskedQuestionDO, frequentlyAskedQuestionDetailVO);
		return frequentlyAskedQuestionDetailVO;
	}
	public boolean isInteger(String str) {
		//判断当前字符串是否为数字类型
		Pattern pattern = Pattern.compile("^[-\\\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
}

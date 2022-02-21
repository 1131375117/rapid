package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.ContentType;
import cn.huacloud.taxpreference.common.enums.ReplyStatus;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dtos.SysCodeStringDTO;
import cn.huacloud.taxpreference.services.consumer.CommonSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationDTO;
import cn.huacloud.taxpreference.services.producer.ConsultationService;
import cn.huacloud.taxpreference.services.producer.entity.dos.ConsultationContentDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.ConsultationDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ConsultationReplyDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryConsultationDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ConsultationContentVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ConsultationVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryConsultationVO;
import cn.huacloud.taxpreference.services.producer.mapper.ConsultationContentMapper;
import cn.huacloud.taxpreference.services.producer.mapper.ConsultationMapper;
import cn.huacloud.taxpreference.sync.es.trigger.impl.ConsultationEventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {
    private final ConsultationMapper consultationMapper;
    private final ConsultationContentMapper consultationContentMapper;
    private final SysCodeService sysCodeService;
    private final ConsultationEventTrigger consultationEventTrigger;
    private final CommonSearchService commonSearchService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveConsultation(ConsultationDTO consultationDTO) {
        //热门咨询表
        ConsultationDO consultationDO = new ConsultationDO();
        copyProperties(consultationDTO, consultationDO);

        consultationMapper.insert(consultationDO);
        LambdaQueryWrapper<ConsultationContentDO> queryWrapper = Wrappers.lambdaQuery(ConsultationContentDO.class).eq(ConsultationContentDO::getConsultationId, consultationDO.getId());
        Long count = consultationContentMapper.selectCount(queryWrapper);

        //热门咨询内容表
        ConsultationContentDO consultationContentDO = new ConsultationContentDO();
        BeanUtils.copyProperties(consultationDTO, consultationContentDO);
        consultationContentDO.setContentType(ContentType.QUESTION);
        consultationContentDO.setSort((int) (1 + count));
        if (!CollectionUtils.isEmpty(consultationDTO.getImageUris())) {
            consultationContentDO.setImageUris(StringUtils.join(consultationDTO.getImageUris(), ","));
        }
        consultationContentDO.setCreateTime(LocalDateTime.now());
        consultationContentDO.setConsultationId(consultationDO.getId());
        consultationContentMapper.insert(consultationContentDO);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyConsultation(ConsultationReplyDTO consultationReplyDTO) {
        //热门咨询表
        ConsultationDO consultationDO = new ConsultationDO();
        consultationDO.setFinishTime(LocalDateTime.now());
        consultationDO.setStatus(ReplyStatus.HAVE_REPLY);
        consultationDO.setProfessorUserId(consultationReplyDTO.getProfessorUserId());
        consultationDO.setId(consultationReplyDTO.getConsultationId());
        consultationMapper.updateById(consultationDO);
        LambdaQueryWrapper<ConsultationContentDO> queryWrapper = Wrappers.lambdaQuery(ConsultationContentDO.class).eq(ConsultationContentDO::getConsultationId, consultationDO.getId());
        Long count = consultationContentMapper.selectCount(queryWrapper);
        //热门咨询content表
        ConsultationContentDO consultationContentDO = new ConsultationContentDO();
        BeanUtils.copyProperties(consultationReplyDTO, consultationContentDO);
        consultationContentDO.setContentType(ContentType.ANSWER);
        consultationContentDO.setSort((int) (1 + count));
        if (!CollectionUtils.isEmpty(consultationReplyDTO.getImageUris())) {
            consultationContentDO.setImageUris(StringUtils.join(consultationReplyDTO.getImageUris(), ","));
        }
        consultationContentDO.setCreateTime(LocalDateTime.now());
        consultationContentMapper.insert(consultationContentDO);
        //写入es
        consultationEventTrigger.saveEvent(consultationDO.getId());

    }

    @Override
    public ConsultationVO consultationDetail(Long id) {
        ConsultationVO consultationVO = new ConsultationVO();
        ConsultationDO consultationDO = consultationMapper.selectById(id);
        BeanUtils.copyProperties(consultationDO, consultationVO);
        if (!StringUtils.isEmpty(consultationDO.getIndustryNames())) {
            consultationVO.setIndustryNames(Arrays.asList(consultationDO.getIndustryNames().split(",")));
        }
        if (!StringUtils.isEmpty(consultationDO.getIndustryCodes())) {
            consultationVO.setIndustryCodes(Arrays.asList(consultationDO.getIndustryCodes().split(",")));
        }
        if (!StringUtils.isEmpty(consultationDO.getTaxCategoriesCodes())) {
            consultationVO.setTaxCategoriesCodes(Arrays.asList(consultationDO.getTaxCategoriesCodes().split(",")));
        }
        if (!StringUtils.isEmpty(consultationDO.getTaxCategoriesNames())) {
            consultationVO.setTaxCategoriesNames(Arrays.asList(consultationDO.getTaxCategoriesNames().split(",")));
        }
        LambdaQueryWrapper<ConsultationContentDO> queryWrapper = Wrappers.lambdaQuery(ConsultationContentDO.class).eq(ConsultationContentDO::getConsultationId, id);
        List<ConsultationContentDO> consultationContentDOList = consultationContentMapper.selectList(queryWrapper);
        List<ConsultationContentVO> consultationContentVOList = new ArrayList<>();

        for (ConsultationContentDO consultationContentDO : consultationContentDOList) {
            ConsultationContentVO consultationContentVO = new ConsultationContentVO();
            BeanUtils.copyProperties(consultationContentDO, consultationContentVO);
            if (!StringUtils.isEmpty(consultationContentDO.getImageUris())) {
                consultationContentVO.setImageUris(Arrays.asList(consultationContentDO.getImageUris().split(",")));
            }
            consultationContentVOList.add(consultationContentVO);
        }
        consultationVO.setConsultationContentVO(consultationContentVOList);
        return consultationVO;
    }

    @Override
    public ResultVO<PageVO<QueryConsultationVO>> queryConsultationList(QueryConsultationDTO queryConsultationDTO) {
        //查询list
        Page<QueryConsultationVO> page =
                new Page<>(queryConsultationDTO.getPageNum(), queryConsultationDTO.getPageSize());
        IPage<QueryConsultationVO> iPage =
                consultationMapper.queryConsultationList(page, queryConsultationDTO);
        List<QueryConsultationVO> records = iPage.getRecords();
        PageVO<QueryConsultationVO> pageVO = PageVO.createPageVO(iPage, records);
        return ResultVO.ok(pageVO);
    }

    private void copyProperties(ConsultationDTO consultationDTO, ConsultationDO consultationDO) {
        BeanUtils.copyProperties(consultationDTO, consultationDO);
        SysCodeStringDTO industries = sysCodeService.getSysCodeStringDTO(SysCodeType.INDUSTRY, consultationDTO.getIndustryCodes(), false);
        SysCodeStringDTO taxCategories = sysCodeService.getSysCodeStringDTO(SysCodeType.TAX_CATEGORIES, consultationDTO.getTaxCategoriesCodes(), false);
        // 处理税种转换
        consultationDO.setTaxCategoriesNames(taxCategories.getNames());
        consultationDO.setTaxCategoriesCodes(taxCategories.getCodes());
        consultationDO.setIndustryCodes(industries.getCodes());
        consultationDO.setIndustryNames(industries.getNames());

        consultationDO.setStatus(ReplyStatus.NOT_REPLY);
        consultationDO.setCreateTime(LocalDateTime.now());
    }
}

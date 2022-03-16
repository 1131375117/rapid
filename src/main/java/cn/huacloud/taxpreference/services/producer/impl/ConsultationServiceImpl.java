package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.*;
import cn.huacloud.taxpreference.common.utils.RedisKeyUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dtos.SysCodeStringDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.AppendConsultationDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationDTO;
import cn.huacloud.taxpreference.services.message.EmailService;
import cn.huacloud.taxpreference.services.message.SmsService;
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
import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import cn.huacloud.taxpreference.services.user.mapper.ConsumerUserMapper;
import cn.huacloud.taxpreference.sync.es.trigger.impl.ConsultationEventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private final SmsService smsService;
    private final EmailService emailService;
    private final ConsumerUserMapper consumerUserMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveConsultation(ConsultationDTO consultationDTO) {
        //热门咨询表
        ConsultationDO consultationDO = new ConsultationDO();
        copyProperties(consultationDTO, consultationDO);
        consultationDO.setFinishTime(LocalDateTime.now());

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
        consultationEventTrigger.saveEvent(consultationDO.getId());
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

        //发送短信
        ConsultationDO selectById = consultationMapper.selectById(consultationReplyDTO.getConsultationId());
        smsService.sendSms(String.valueOf(selectById.getPhoneNumber()), SmsBiz.CONSULTATION_REPLAY);

        //发送邮件
        Long customerUserId = selectById.getCustomerUserId();
        ConsumerUserDO consumerUserDO = consumerUserMapper.selectById(customerUserId);
        if (!StringUtils.isEmpty(consumerUserDO.getEmail())) {
            emailService.sendEmail(consumerUserDO.getEmail(), EmailBiz.CONSULTATION_REPLY);
        }

        //写入redis
        stringRedisTemplate.opsForValue().set(RedisKeyUtil.getConsultationReplyRedisKey(customerUserId), String.valueOf(RedPointStatus.SHOW),7L, TimeUnit.DAYS);

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
        Integer pageNum = queryConsultationDTO.getPageNum();
        Integer pageSize = queryConsultationDTO.getPageSize();
        List<QueryConsultationVO> voList = consultationMapper.queryConsultationList((pageNum - 1) * pageSize, pageSize, queryConsultationDTO);
       // List<QueryConsultationVO> records = iPage.getRecords();
        Long count = consultationMapper.selectCountByConsultationId(queryConsultationDTO);
        PageVO<QueryConsultationVO> pageVO=new PageVO<>();
        pageVO.setTotal(count);
        pageVO.setPageNum(pageNum);
        pageVO.setPageSize(pageSize);
        pageVO.setRecords(voList);
        //PageVO<QueryConsultationVO> pageVO = PageVO.createPageVO(iPage, records);
        return ResultVO.ok(pageVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void appendConsultation(AppendConsultationDTO consultationDTO) {
        LambdaQueryWrapper<ConsultationContentDO> queryWrapper = Wrappers.lambdaQuery(ConsultationContentDO.class)
                .eq(ConsultationContentDO::getConsultationId, consultationDTO.getConsultationId());
        Long count = consultationContentMapper.selectCount(queryWrapper);

        //修改答复状态
        ConsultationDO consultationDO = new ConsultationDO();
        consultationDO.setId(consultationDTO.getConsultationId()).setStatus(ReplyStatus.NOT_REPLY);
        consultationDO.setFinishTime(LocalDateTime.now());
        consultationMapper.updateById(consultationDO);

        List<ConsultationContentDO> consultationContentDOS = consultationContentMapper.selectList(queryWrapper);
        List<ConsultationContentDO> collect = consultationContentDOS.stream().filter(consultationContentDO -> consultationContentDO.getContentType().equals(ContentType.QUESTION)).collect(Collectors.toList());
        if (collect.size() >= 4) {
            throw BizCode._4608.exception();
        }
        //热门咨询内容表
        ConsultationContentDO consultationContentDO = new ConsultationContentDO();
        BeanUtils.copyProperties(consultationDTO, consultationContentDO);
        consultationContentDO.setContentType(ContentType.QUESTION);
        consultationContentDO.setSort((int) (1 + count));
        if (!CollectionUtils.isEmpty(consultationDTO.getImageUris())) {
            consultationContentDO.setImageUris(StringUtils.join(consultationDTO.getImageUris(), ","));
        }
        consultationContentDO.setCreateTime(LocalDateTime.now());
        consultationContentMapper.insert(consultationContentDO);
        consultationEventTrigger.saveEvent(consultationDO.getId());
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

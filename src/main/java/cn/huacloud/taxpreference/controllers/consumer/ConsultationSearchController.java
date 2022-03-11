package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.RedPointStatus;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.ConsultationSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.AppendConsultationDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.MyConsultationDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationCountVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationESVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationListVO;
import cn.huacloud.taxpreference.services.producer.ConsultationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fuhua
 **/
@Api(tags = "热门咨询")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ConsultationSearchController {
    private final ConsultationService consultationService;
    private final ConsultationSearchService consultationSearchService;

    /**
     * 新增
     * 参数列表：docId,收藏类型
     */
    @ApiOperation(value = "添加热门咨询问题")
    @ConsumerUserCheckLogin
    @PostMapping("/consultation")
    public ResultVO<Void> saveConsultation(@Validated @RequestBody ConsultationDTO consultationDTO) {
        if (consultationDTO.getPublished() == null) {
            consultationDTO.setPublished(1L);
        }
        consultationDTO.setCustomerUserId(ConsumerUserUtil.getCurrentUser().getId());
        consultationService.saveConsultation(consultationDTO);
        return ResultVO.ok();
    }

    /**
     * 新增
     * 参数列表：docId,收藏类型
     */
    @ApiOperation(value = "追问热门咨询问题")
    @ConsumerUserCheckLogin
    @PutMapping("/consultation")
    public ResultVO<Void> appendConsultation(@Validated @RequestBody AppendConsultationDTO consultationDTO) {
        consultationService.appendConsultation(consultationDTO);
        return ResultVO.ok();
    }

    @ApiOperation("热门咨询检索")
    @ConsumerUserCheckLogin
    @PostMapping("/consultationQuery")
    public ResultVO<PageVO<ConsultationESVO>> pageSearch(@RequestBody ConsultationQueryDTO pageQuery) throws Exception {
        pageQuery.setPublished(1L);
        PageVO<ConsultationESVO> pageVO = consultationSearchService.pageSearch(pageQuery);
        List<ConsultationESVO> records = pageVO.getRecords();
        for (ConsultationESVO record : records) {
            record.setConsultationContent(Collections.singletonList(record.getConsultationContent().get(0)));
        }
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("我的热门咨询,若专家回复需要提示小红点")
    @GetMapping("/consultation/my")
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<ConsultationESVO>> myConsultation(PageQueryDTO pageQuery) throws Exception {

        pageQuery.paramReasonable();
        PageVO<ConsultationESVO> page = consultationSearchService.myConsultation(pageQuery, ConsumerUserUtil.getCurrentUserId());
        return ResultVO.ok(page);
    }

    @ApiOperation("热门咨询（推荐）")
    @GetMapping("/consultation/hot")
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<ConsultationESVO>> hotConsultation(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<ConsultationESVO> page = consultationSearchService.hotConsultation(pageQuery);
        return ResultVO.ok(page);
    }

    @ApiOperation("最新热门咨询")
    @GetMapping("/consultation/latest")
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<ConsultationESVO>> latestTaxPreference(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<ConsultationESVO> page = consultationSearchService.latestConsultation(pageQuery);
        return ResultVO.ok(page);
    }

    @ApiOperation("热门咨询详情")
    @GetMapping("/consultation/{id}")
    @ConsumerUserCheckLogin
    public ResultVO<ConsultationESVO> pageSearch(@PathVariable("id") Long id) throws Exception {
        ConsultationESVO consultationESVO = consultationSearchService.getConsultationDetails(id);
        return ResultVO.ok(consultationESVO);
    }

    @ApiOperation("热门咨询数据统计")
    @GetMapping("/consultation")
    @ConsumerUserCheckLogin
    public ResultVO<ConsultationCountVO> getCount() throws Exception {
        ConsultationCountVO count = consultationSearchService.getCount();
        return ResultVO.ok(count);
    }

    @ApiOperation("近似推荐")
    @PostMapping("/approximateConsultation")
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<ConsultationESVO>> approximateConsultation(@RequestBody ConsultationQueryDTO pageQuery) throws Exception {
        PageVO<ConsultationESVO> pageVO = consultationSearchService.pageSearch(pageQuery);
        pageVO.setRecords(pageVO.getRecords()
                .stream()
                .peek(consultationESVO
                        -> consultationESVO.setFirstQuestTime(
                        consultationESVO.getConsultationContent()
                                .get(0)
                                .getCreateTime()))
                .collect(Collectors.toList()))
        ;
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("前端小红点消息提示，HIDDEN-隐藏,SHOW-显示")
    @PostMapping("/replyTipsConsultation")
    @ConsumerUserCheckLogin
    public ResultVO<RedPointStatus> replyTipsConsultation() throws IOException {
        RedPointStatus flag = consultationSearchService.replyTipsConsultation(ConsumerUserUtil.getCurrentUserId());
        return ResultVO.ok(flag);
    }


    @ApiOperation("个人中心-我的咨询")
    @GetMapping("/myConsultation")
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<ConsultationListVO>> myConsultation(MyConsultationDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        Long currentUserId = ConsumerUserUtil.getCurrentUserId();
        pageQuery.setCustomerUserId(currentUserId);
        PageVO<ConsultationESVO> pageVO = consultationSearchService.pageSearch(pageQuery);
        //封装返回结果
        PageVO<ConsultationListVO> consultationListVOPageVO = new PageVO<>();
        //List
        List<ConsultationListVO> consultationListVOS = new ArrayList<>();

        List<ConsultationESVO> records = pageVO.getRecords();
        for (ConsultationESVO record : records) {
            ConsultationListVO consultationCountVO = new ConsultationListVO();
            consultationCountVO.setConsultationContent(
                    record.getConsultationContent().get(0).getContent());
            BeanUtils.copyProperties(record, consultationCountVO);
            consultationCountVO.setCreateTime(record.getConsultationContent().get(0).getCreateTime());
            if (CollectionUtils.isEmpty(record.getIndustries())) {
                consultationCountVO.setTaxCategories(new ArrayList<>());
            } else {
                consultationCountVO.setTaxCategories(Collections.singletonList(record.getIndustries().get(0).getCodeName()));
            }
            consultationListVOS.add(consultationCountVO);
        }

        BeanUtils.copyProperties(pageVO, consultationListVOPageVO);
        consultationListVOPageVO.setRecords(consultationListVOS);
        return ResultVO.ok(consultationListVOPageVO);
    }
}

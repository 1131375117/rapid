package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.ConsultationSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ApproximateConsultationDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationCountVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationESVO;
import cn.huacloud.taxpreference.services.producer.ConsultationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
        consultationDTO.setCustomerUserId(ConsumerUserUtil.getCurrentUser().getId());
        consultationService.saveConsultation(consultationDTO);
        return ResultVO.ok();
    }

    @ApiOperation("热门咨询检索")
    @ConsumerUserCheckLogin
    @PostMapping("/consultationQuery")
    public ResultVO<PageVO<ConsultationESVO>> pageSearch(@RequestBody ConsultationQueryDTO pageQuery) throws Exception {
        PageVO<ConsultationESVO> pageVO = consultationSearchService.pageSearch(pageQuery);
        List<ConsultationESVO> records = pageVO.getRecords();
        for (ConsultationESVO record : records) {
            record.setConsultationContent(Collections.singletonList(record.getConsultationContent().get(0)));
        }
        return ResultVO.ok(pageVO);
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
    public ResultVO<PageVO<ConsultationESVO>> approximateConsultation(@RequestBody ApproximateConsultationDTO approximateConsultationDTO) throws Exception {
        PageVO<ConsultationESVO> pageVO = consultationSearchService.approximateConsultation(approximateConsultationDTO);
        return ResultVO.ok(pageVO);
    }
}

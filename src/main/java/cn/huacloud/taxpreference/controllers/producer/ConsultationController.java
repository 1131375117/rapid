package cn.huacloud.taxpreference.controllers.producer;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ProducerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.ConsultationService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ConsultationReplyDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryConsultationDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ConsultationVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryConsultationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 热门咨询
 *
 * @author fuhua
 **/
@Api(tags = "热门咨询")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ConsultationController {
    private final ConsultationService consultationService;

    @PermissionInfo(name = "热门咨询查询接口", group = PermissionGroup.HOT_CONSULTATION)
    @SaCheckPermission("producer_consultation_query")
    @ApiOperation("热门咨询查询接口")
    @PostMapping("queryConsultation")
    public ResultVO<PageVO<QueryConsultationVO>> queryConsultation(@RequestBody QueryConsultationDTO queryConsultationDTO) {
        queryConsultationDTO.paramReasonable();
        return consultationService.queryConsultationList(queryConsultationDTO);
    }

    /**
     * 新增replay
     */
    @PermissionInfo(name = "热门咨询回复", group = PermissionGroup.HOT_CONSULTATION)
    @SaCheckPermission("producer_consultation_reply_insert")
    @ApiOperation("热门咨询回复")
    @PostMapping(value = "/consultationReply")
    public ResultVO<Void> consultationReply(@Validated @RequestBody ConsultationReplyDTO consultationReplyDTO) {
        consultationReplyDTO.setProfessorUserId(ProducerUserUtil.getCurrentUserId());
        consultationService.replyConsultation(consultationReplyDTO);
        return ResultVO.ok();
    }


    @PermissionInfo(name = "热门咨询详情", group = PermissionGroup.HOT_CONSULTATION)
    @SaCheckPermission("producer_consultation_detail")
    @ApiOperation("热门咨询详情")
    @GetMapping(value = "/consultationDetail/{id}")
    public ResultVO<ConsultationVO> consultationDetail(@NotNull(message = "id不能为空") @PathVariable Long id) {
        ConsultationVO consultationVO = consultationService.consultationDetail(id);
        return ResultVO.ok(consultationVO);
    }
}

package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.OperationRecordService;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.common.entity.dtos.ViewQueryDTO;
import cn.huacloud.taxpreference.services.common.entity.vos.OperationRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 操作记录
 *
 * @author fuhua
 **/
@Api(tags = "操作记录")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class OperationRecordController {

    private final OperationRecordService operationRecord;

    /**
     * 操作记录:前台数据操作一次调用一次
     * 参数：操作类型,操作参数
     */
    @ApiOperation("操作记录接口")
    @PostMapping("/operationRecord")
    public ResultVO<Void> operationRecord(@RequestBody @Validated OperationRecordDTO operationRecordDTO) {
        if (ConsumerUserUtil.isLogin()) {
            Long consumerUserId = ConsumerUserUtil.getCurrentUserId();
            operationRecord.saveOperationRecord(operationRecordDTO, consumerUserId);
        }
        return ResultVO.ok();
    }

    @ApiOperation("操作记录列表")
    @PostMapping("/queryOperationRecord")
    public ResultVO<PageVO<OperationRecordVO>> queryOperationRecord(@RequestBody ViewQueryDTO pageQueryDTO) {
        PageVO<OperationRecordVO> pageVO = new PageVO<>();
        pageQueryDTO.paramReasonable();
        if (ConsumerUserUtil.isLogin()) {
            Long consumerUserId = ConsumerUserUtil.getCurrentUserId();
            pageVO = operationRecord.queryOperationRecord(pageQueryDTO, consumerUserId);
        }
        return ResultVO.ok(pageVO);
    }
}

package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.OperationRecordService;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.ConsumerLoginUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            ConsumerLoginUserVO currentUser = ConsumerUserUtil.getCurrentUser();
            Long consumerUserId = ConsumerUserUtil.getCurrentUserId();
            operationRecord.operationRecord(operationRecordDTO, consumerUserId);
        }
        return ResultVO.ok();
    }
}

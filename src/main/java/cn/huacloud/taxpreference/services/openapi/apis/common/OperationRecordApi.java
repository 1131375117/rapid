package cn.huacloud.taxpreference.services.openapi.apis.common;

import cn.huacloud.taxpreference.common.annotations.LimitApi;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.entity.vos.OperationRecordVO;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiCheckOpenUserId;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.common.OperationRecordService;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.common.entity.dtos.ViewQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PageByOperationVO;
import cn.huacloud.taxpreference.services.openapi.monitor.MonitorApi;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
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
@RequiredArgsConstructor
@ApiSupport(order = 900)
@Api(tags = "操作记录")
@RequestMapping("/open-api/v1")
@RestController
public class OperationRecordApi {

    private final OperationRecordService operationRecord;

    /**
     * 操作记录:前台数据操作一次调用一次
     * 参数：操作类型,操作参数
     */
    @ApiOperation("添加操作记录")
    @OpenApiCheckOpenUserId
    @MonitorApi
    @LimitApi
    @OpenApiCheckToken
    @PostMapping("/operationRecord")
    public ResultVO<Void> operationRecord(@RequestBody @Validated OperationRecordDTO operationRecordDTO) {
        if (OpenApiStpUtil.isLogin()) {
            Long consumerUserId = OpenApiStpUtil.getConsumerUserId();
            operationRecord.saveOperationRecord(operationRecordDTO, consumerUserId);
        }
        return ResultVO.ok();
    }

    @ApiOperation("查询操作记录")
    @OpenApiCheckOpenUserId
    @MonitorApi
    @LimitApi
    @OpenApiCheckToken
    @PostMapping("/queryOperationRecord")
    public ResultVO<PageByOperationVO<OperationRecordVO>> queryOperationRecord(@RequestBody ViewQueryDTO pageQueryDTO) {
        PageByOperationVO<OperationRecordVO> pageVO = null;
        pageQueryDTO.paramReasonable();
        if (OpenApiStpUtil.isLogin()) {
            Long consumerUserId = OpenApiStpUtil.getConsumerUserId();
            pageVO = operationRecord.queryOperationRecord(pageQueryDTO, consumerUserId);
        }
        return ResultVO.ok(pageVO);
    }
}

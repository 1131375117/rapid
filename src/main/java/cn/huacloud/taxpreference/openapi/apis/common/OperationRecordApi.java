package cn.huacloud.taxpreference.openapi.apis.common;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.common.OperationRecordService;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.common.entity.dtos.ViewQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PageByOperationVO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 操作记录
 *
 * @author fuhua
 **/
@RequiredArgsConstructor
/*@ApiSupport(order = 900)
@Api(tags = "操作记录")
@RequestMapping("/open-api/v1")
@RestController*/
public class OperationRecordApi {

    private final OperationRecordService operationRecord;

    /**
     * 操作记录:前台数据操作一次调用一次
     * 参数：操作类型,操作参数
     */
    @ApiOperation("操作记录接口")
    @OpenApiCheckToken
    @PostMapping("/operationRecord")
    public ResultVO<Void> operationRecord(@RequestBody @Validated OperationRecordDTO operationRecordDTO) {
        if (OpenApiStpUtil.isLogin()) {
            Long consumerUserId = OpenApiStpUtil.getLoginIdAsLong();
            operationRecord.saveOperationRecord(operationRecordDTO, consumerUserId);
        }
        return ResultVO.ok();
    }

    @ApiOperation("操作记录列表")
    @OpenApiCheckToken
    @PostMapping("/queryOperationRecord")
    public ResultVO<PageByOperationVO> queryOperationRecord(@RequestBody ViewQueryDTO pageQueryDTO) {
        PageByOperationVO pageVO = null;
        pageQueryDTO.paramReasonable();
        if (OpenApiStpUtil.isLogin()) {
            Long consumerUserId = OpenApiStpUtil.getLoginIdAsLong();
            ;
            pageVO = operationRecord.queryOperationRecord(pageQueryDTO, consumerUserId);
        }
        return ResultVO.ok(pageVO);
    }
}

package cn.huacloud.taxpreference.services.openapi.apis.consumer;

import cn.huacloud.taxpreference.common.annotations.LimitApi;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.CollectionVO;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiCheckOpenUserId;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.common.entity.dtos.CollectionQueryDTO;
import cn.huacloud.taxpreference.services.consumer.CollectionService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.CollectionDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PageByCollectionVO;
import cn.huacloud.taxpreference.services.openapi.monitor.MonitorApi;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏功能
 *
 * @author fuhua
 **/
@RequiredArgsConstructor
@ApiSupport(order = 800)
@Api(tags = "收藏功能")
@RequestMapping("/open-api/v1")
@RestController
public class CollectionApi {

    private final CollectionService collectionService;

    /**
     * 收藏功能:点击收藏添加到我的收藏,再次点击取消收藏,未登录用户需要先登录
     * 参数列表：docId,收藏类型
     */
    @ApiOperation(value = "添加到收藏列表", notes = "返回值boolean表示收藏的当前状态，true：已收藏；false：未收藏")
    @OpenApiCheckOpenUserId
    @MonitorApi
    @LimitApi
    @OpenApiCheckToken
    @PutMapping("/collection")
    public ResultVO<Boolean> saveOrCancelCollection(@RequestBody @Validated CollectionDTO collectionDTO) {
        collectionDTO.setConsumerUserId(OpenApiStpUtil.getConsumerUserId());
        Boolean isCollection = collectionService.saveOrCancelCollection(collectionDTO);
        return ResultVO.ok(isCollection);
    }

    /**
     * 收藏展示功能:
     * 参数列表：无
     */
    @ApiOperation("查询收藏列表")
    @OpenApiCheckOpenUserId
    @MonitorApi
    @LimitApi
    @OpenApiCheckToken
    @PostMapping("/queryCollection")
    public ResultVO<PageVO<DateRecords<CollectionVO>>> queryCollection(@RequestBody CollectionQueryDTO pageQueryDTO) {
        pageQueryDTO.paramReasonable();
        PageByCollectionVO pageVO = collectionService.queryCollection(pageQueryDTO, OpenApiStpUtil.getConsumerUserId());
        // TODO 返回记录不结构不统一，这里对OpenAPI单独进行调整
        PageVO<DateRecords<CollectionVO>> newPage = new PageVO<>();
        newPage.setPageNum(pageVO.getPageNum());
        newPage.setPageSize(pageVO.getPageSize());
        newPage.setTotal(pageVO.getTotal());

        List<DateRecords<CollectionVO>> records = pageVO.getList().stream().map(vo -> {
            DateRecords<CollectionVO> dateRecords = new DateRecords<>();
            dateRecords.setDate(vo.getDate());
            List<CollectionVO> pageVOList = vo.getPageVOList();
            dateRecords.setSubRecords(pageVOList);
            return dateRecords;
        }).collect(Collectors.toList());
        newPage.setRecords(records);
        return ResultVO.ok(newPage);
    }

    /**
     * 嫌弃 cn.huacloud.taxpreference.services.consumer.entity.vos.CollectionPageVO 的命名
     * @param <T>
     */
    @Data
    public static class DateRecords<T> {
        @ApiModelProperty("日期")
        private LocalDate date;
        @ApiModelProperty("子记录")
        private List<T> subRecords;
    }
}

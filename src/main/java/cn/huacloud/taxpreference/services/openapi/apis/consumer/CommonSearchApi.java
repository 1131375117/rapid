package cn.huacloud.taxpreference.services.openapi.apis.consumer;

import cn.huacloud.taxpreference.common.annotations.LimitApi;
import cn.huacloud.taxpreference.services.openapi.monitor.MonitorApi;
import cn.huacloud.taxpreference.common.entity.dtos.ExSearchQueryDTO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeCountVO;
import cn.huacloud.taxpreference.services.consumer.CommonSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.vos.SearchDataCountVO;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangkh
 */
@ApiSupport(order = 300)
@Api(tags = "公共检索")
@RequiredArgsConstructor
@RequestMapping("/open-api/v1/search")
@RestController
public class CommonSearchApi {

    private final CommonSearchService commonSearchService;

    @ApiOperation("数据统计（首页）")
    @OpenApiCheckToken
    @MonitorApi
    @LimitApi
    @GetMapping("/dataCount")
    public ResultVO<SearchDataCountVO> getDataCount() throws Exception {
        SearchDataCountVO searchDataCountVO = commonSearchService.getDataCount();
        return ResultVO.ok(searchDataCountVO);
    }

    @ApiOperation("高级搜索统计所有文档数")
    @OpenApiCheckToken
    @MonitorApi
    @LimitApi
    @PostMapping("/allDocCount")
    public ResultVO<List<SysCodeCountVO>> allDocCount(@RequestBody ExSearchQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        List<SysCodeCountVO> sysCodeCountVOList = commonSearchService.allDocCount(pageQuery);
        return ResultVO.ok(sysCodeCountVOList);
    }
}

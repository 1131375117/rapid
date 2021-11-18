package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.TaxPreferenceSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.TaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotLabelVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.TaxPreferenceSearchVO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.ResultType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangkh
 */
@Api(tags = "税收优惠检索")
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class TaxPreferenceSearchController {

    private final TaxPreferenceSearchService taxPreferenceSearchService;

    @ApiOperation("税收优惠搜索")
    @PostMapping("/taxPreference")
    public ResultVO<PageVO<TaxPreferenceSearchVO>> pageSearch(@RequestBody TaxPreferenceSearchQueryDTO pageQuery) throws Exception {
        PageVO<TaxPreferenceSearchVO> pageVO = taxPreferenceSearchService.pageSearch(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("税收优惠详情")
    @GetMapping("/taxPreference/{id}")
    public ResultVO<TaxPreferenceSearchVO> getTaxPreferenceDetails(@PathVariable("id") Long id) throws Exception {
        TaxPreferenceSearchVO taxPreferenceSearchVO = taxPreferenceSearchService.getTaxPreferenceDetails(id);
        return ResultVO.ok(taxPreferenceSearchVO);
    }

    @ApiOperation("热门标签列表")
    @GetMapping("/taxPreference/hotLabels")
    public ResultVO<List<HotLabelVO>> hotLabels(@RequestParam(value = "size", defaultValue = "30") Integer size) throws Exception {
        List<HotLabelVO> hotLabels = taxPreferenceSearchService.hotLabels(size);
        return ResultVO.ok(hotLabels);
    }
}

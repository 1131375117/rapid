package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.TaxPreferenceSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.LatestTaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.TaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.DocSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotLabelVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.TaxPreferenceSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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

    @ApiOperation("热门税收优惠（首页）")
    @GetMapping("/taxPreference/hot")
    public ResultVO<PageVO<DocSearchSimpleVO>> hotTaxPreference(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<DocSearchSimpleVO> page = taxPreferenceSearchService.hotTaxPreference(pageQuery);
        return ResultVO.ok(page);
    }

    @ApiOperation("最新税收优惠（首页）")
    @GetMapping("/taxPreference/latest")
    public ResultVO<PageVO<DocSearchSimpleVO>> latestTaxPreference(LatestTaxPreferenceSearchQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<DocSearchSimpleVO> page = taxPreferenceSearchService.latestTaxPreference(pageQuery);
        return ResultVO.ok(page);
    }

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

package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.TaxPreferenceSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.LatestTaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.TaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<TaxPreferenceSearchListVO>> pageSearch(@RequestBody TaxPreferenceSearchQueryDTO pageQuery) throws Exception {
        PageVO<TaxPreferenceSearchListVO> pageVO = taxPreferenceSearchService.pageSearch(pageQuery);
        // 添加序号
        List<TaxPreferenceSearchListVO> records = pageVO.getRecords();
        long startNum = (pageVO.getPageNum() - 1L) * pageVO.getPageSize() + 1;
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setNum(startNum + i);
        }
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("税收优惠详情")
    @GetMapping("/taxPreference/{id}")
    @ConsumerUserCheckLogin
    public ResultVO<TaxPreferenceSearchVO> getTaxPreferenceDetails(@PathVariable("id") Long id) throws Exception {
        TaxPreferenceSearchVO taxPreferenceSearchVO = taxPreferenceSearchService.getTaxPreferenceDetails(id);
        taxPreferenceSearchVO.initUserCollectionInfo(CollectionType.TAX_PREFERENCE);
        return ResultVO.ok(taxPreferenceSearchVO);
    }

    @ApiOperation("热门标签列表")
    @GetMapping("/taxPreference/hotLabels")
    @ConsumerUserCheckLogin
    public ResultVO<List<HotLabelVO>> hotLabels(@RequestParam(value = "size", defaultValue = "30") Integer size) throws Exception {
        List<HotLabelVO> hotLabels = taxPreferenceSearchService.hotLabels(size);
        return ResultVO.ok(hotLabels);
    }

    @ApiOperation("根据条件参数动态获取筛选条件")
    @PostMapping("/taxPreference/dynamicCondition")
    @ConsumerUserCheckLogin
    public ResultVO<DynamicConditionVO> getDynamicCondition(@RequestBody TaxPreferenceSearchQueryDTO pageQuery) throws Exception {
        pageQuery.setKeyword(null);
        pageQuery.setConditions(new ArrayList<>());
        pageQuery.paramReasonable();
        DynamicConditionVO dynamicConditionVO = taxPreferenceSearchService.getDynamicCondition(pageQuery);
        return ResultVO.ok(dynamicConditionVO);
    }

    @ApiOperation("获取减免事项关联的税种码值")
    @PostMapping("/taxPreference/itemRelatedCodes")
    @ConsumerUserCheckLogin
    public ResultVO<List<String>> getItemRelatedCodes(@RequestBody List<String> taxPreferenceItems) throws Exception {
        List<String> itemRelatedCodes = taxPreferenceSearchService.getItemRelatedCodes(taxPreferenceItems);
        return ResultVO.ok(itemRelatedCodes);
    }
}

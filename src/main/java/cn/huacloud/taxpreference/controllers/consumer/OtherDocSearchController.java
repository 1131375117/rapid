package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.OtherDocSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.DocSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 案例检索
 *
 * @author fuhua
 **/
@Api(tags = "案例分析检索")
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class OtherDocSearchController {

    private final OtherDocSearchService otherDocSearchService;

    @ApiOperation("最新案例解析（首页）")
    @GetMapping("/otherDoc/caseAnalyse/latest")
    public ResultVO<PageVO<DocSearchSimpleVO>> latestCaseAnalyse(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<DocSearchSimpleVO> page = otherDocSearchService.latestCaseAnalyse(pageQuery);
        return ResultVO.ok(page);
    }

    @ApiOperation("案例分析检索")
    @PostMapping("/otherDoc")
    public ResultVO<PageVO<OtherDocVO>> pageSearch(@RequestBody OtherDocQueryDTO pageQuery) throws Exception {
        PageVO<OtherDocVO> pageVO = otherDocSearchService.pageSearch(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("获取所有案例分析分类")
    @GetMapping("/otherDoc/caseType")
    public ResultVO<List<String>> getCaseAnalyseType(@RequestParam(value = "size", defaultValue = "200") Integer size) throws Exception {
        List<String> caseAnalyseTypeList = otherDocSearchService.getCaseAnalyseType(size);
        return ResultVO.ok(caseAnalyseTypeList);
    }


    @ApiOperation("案例分析检索详情")
    @GetMapping("/otherDoc/{id}")
    public ResultVO<OtherDocVO> pageSearch(@PathVariable("id") Long id) throws Exception {
        OtherDocVO otherDocVO = otherDocSearchService.getTaxOtherDocDetails(id);
        otherDocVO.initUserCollectionInfo(CollectionType.CASE_ANALYSIS);
        return ResultVO.ok(otherDocVO);
    }
}

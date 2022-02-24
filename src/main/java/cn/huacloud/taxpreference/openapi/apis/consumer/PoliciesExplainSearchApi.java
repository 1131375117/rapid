package cn.huacloud.taxpreference.openapi.apis.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.services.consumer.PoliciesExplainSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesExplainSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchListVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangkh
 */
@Api(tags = "政策解读检索")
@RequiredArgsConstructor
@RequestMapping("/open-api/v1/search")
@RestController
public class PoliciesExplainSearchApi {

    private final PoliciesExplainSearchService policiesExplainSearchService;

    @ApiOperation("最新政策解读（首页）")
    @OpenApiCheckToken
    @GetMapping("/policiesExplain/latest")
    public ResultVO<PageVO<PoliciesExplainSearchSimpleVO>> latestPoliciesExplain(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<PoliciesExplainSearchSimpleVO> page = policiesExplainSearchService.latestPoliciesExplain(pageQuery);
        return ResultVO.ok(page);
    }

    @ApiOperation("政策解读检索")
    @OpenApiCheckToken
    @PostMapping("/policiesExplain")
    public ResultVO<PageVO<PoliciesExplainSearchListVO>> pageSearch(@RequestBody PoliciesExplainSearchQueryDTO pageQuery) throws Exception {
        PageVO<PoliciesExplainSearchListVO> pageVO = policiesExplainSearchService.pageSearch(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("根据政策ID查询相关政策解读")
    @OpenApiCheckToken
    @GetMapping("policiesExplain/policiesRelated/{policiesId}")
    public ResultVO<List<PoliciesExplainSearchSimpleVO>> policiesRelatedExplain(@PathVariable("policiesId") String policiesId) throws Exception {
        List<PoliciesExplainSearchSimpleVO> list = policiesExplainSearchService.policiesRelatedExplain(policiesId);
        return ResultVO.ok(list);
    }

    @ApiOperation("政策解读详情")
    @OpenApiCheckToken
    @GetMapping("/policiesExplain/{id}")
    public ResultVO<PoliciesExplainSearchVO> pageSearch(@PathVariable("id") Long id) throws Exception {
        PoliciesExplainSearchVO policiesExplainSearchVO = policiesExplainSearchService.getPoliciesExplainDetails(id);
        policiesExplainSearchVO.initUserCollectionInfo(CollectionType.POLICIES_EXPLAIN);
        return ResultVO.ok(policiesExplainSearchVO);
    }
}

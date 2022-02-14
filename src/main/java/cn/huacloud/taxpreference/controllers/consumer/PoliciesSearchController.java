package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.PoliciesSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchListVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangkh
 */
@Api(tags = "政策法规检索")
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class PoliciesSearchController {

    private final PoliciesSearchService policiesSearchService;

    @ApiOperation("最新中央政策（首页）")
    @GetMapping("/policies/latestCentral")
    public ResultVO<PageVO<PoliciesSearchSimpleVO>> latestCentralPolicies(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<PoliciesSearchSimpleVO> page = policiesSearchService.latestCentralPolicies(pageQuery);
        return ResultVO.ok(page);
    }

    @ApiOperation("最新地方政策（首页）")
    @GetMapping("/policies/latestLocal")
    public ResultVO<PageVO<PoliciesSearchSimpleVO>> latestLocalPolicies(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<PoliciesSearchSimpleVO> page = policiesSearchService.latestLocalPolicies(pageQuery);
        return ResultVO.ok(page);
    }

    @ApiOperation("热门政策（首页）")
    @GetMapping("/policies/hot")
    public ResultVO<PageVO<PoliciesSearchSimpleVO>> hotPolicies(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<PoliciesSearchSimpleVO> page = policiesSearchService.hotPolicies(pageQuery);
        return ResultVO.ok(page);
    }

    @ApiOperation(value = "政策法规搜索")
    @PostMapping("/policies")
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<PoliciesSearchListVO>> pageSearch(@RequestBody PoliciesSearchQueryDTO pageQuery) throws Exception {
        PageVO<PoliciesSearchListVO> pageVO = policiesSearchService.pageSearch(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("政策法规详情")
    @GetMapping("/policies/{id}")
    @ConsumerUserCheckLogin
    public ResultVO<PoliciesSearchVO> getPoliciesDetails(@PathVariable("id") Long id) throws Exception {
        PoliciesSearchVO policiesSearchVO = policiesSearchService.getPoliciesDetails(id);
        policiesSearchVO.initUserCollectionInfo(CollectionType.POLICIES);
        return ResultVO.ok(policiesSearchVO);
    }

}

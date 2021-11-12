package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.PoliciesSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangkh
 */
@Api(tags = "政策法规检索")
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class PoliciesSearchController {

    private PoliciesSearchService policiesSearchService;

    // 政策简单列表

    // 最新地方政策

    @ApiOperation("政策法规搜索")
    @PostMapping("/policies")
    public ResultVO<PageVO<PoliciesSearchVO>> pageSearch(@RequestBody PoliciesSearchQueryDTO pageQuery) throws Exception {
        PageVO<PoliciesSearchVO> pageVO = policiesSearchService.pageSearch(pageQuery);
        return ResultVO.ok(pageVO);
    }

    // 政策法规详情


}

package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.OtherDocSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 案例检索
 *
 * @author fuhua
 **/
@Api(tags = "案例分析")
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class OtherDocSearchController {

    private final OtherDocSearchService otherDocSearchService;

    @ApiOperation("案例分析检索")
    @PostMapping("/otherDoc")
    public ResultVO<PageVO<OtherDocVO>> pageSearch(@RequestBody OtherDocDTO pageQuery) throws Exception {
        PageVO<OtherDocVO> pageVO = otherDocSearchService.pageSearch(pageQuery);
        return ResultVO.ok(pageVO);
    }
}

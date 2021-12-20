package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ProducerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeCountVO;
import cn.huacloud.taxpreference.services.consumer.CommonSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.GuessYouLikeQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotContentVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.SearchDataCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangkh
 */
@Api(tags = "公共检索")
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class CommonSearchController {

    private final CommonSearchService commonSearchService;

    @ApiOperation("数据统计（首页）")
    @GetMapping("/dataCount")
    public ResultVO<SearchDataCountVO> getDataCount() throws Exception {
        SearchDataCountVO searchDataCountVO = commonSearchService.getDataCount();
        return ResultVO.ok(searchDataCountVO);
    }

    @ApiOperation("高级搜索统计所有文档数")
    @PostMapping("/allDocCount")
    public ResultVO<List<SysCodeCountVO>> allDocCount(@RequestBody KeywordPageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        List<SysCodeCountVO> sysCodeCountVOList = commonSearchService.allDocCount(pageQuery);
        return ResultVO.ok(sysCodeCountVOList);
    }

    @ApiOperation(value = "本周热点内容", notes = "需要根据 hotContentType.codeValue 跳转不同的页面，POLICIES:政策法规;POLICIES_EXPLAIN:政策解读;FREQUENTLY_ASKED_QUESTION:热门问答;TAX_PREFERENCE:税收优惠;CASE_ANALYSIS:案例分析")
    @GetMapping("/hotContent/weekly")
    public ResultVO<PageVO<HotContentVO>> weeklyHotContent(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<HotContentVO> pageVO = commonSearchService.weeklyHotContent(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("猜你关注")
    @PostMapping("/hotContent/guessYouLike")
    public ResultVO<PageVO<HotContentVO>> guessYouLike(@RequestBody GuessYouLikeQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        if (ProducerUserUtil.isLogin()) {
            pageQuery.setUserId(ProducerUserUtil.getCurrentUserId());
        }
        PageVO<HotContentVO> pageVO = commonSearchService.guessYouLike(pageQuery);
        return ResultVO.ok(pageVO);
    }
}

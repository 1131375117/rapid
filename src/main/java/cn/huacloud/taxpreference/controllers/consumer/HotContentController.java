package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeCountVO;
import cn.huacloud.taxpreference.services.consumer.HotContentService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.GuessYouLikeQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotContentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangkh
 */
@Api(tags = "热点内容检索")
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class HotContentController {

    private final HotContentService hotContentService;

    @ApiOperation("高级搜索统计所有文档数")
    @PostMapping("/allDocCount")
    public ResultVO<List<SysCodeCountVO>> allDocCount(KeywordPageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        List<SysCodeCountVO> sysCodeCountVOList = hotContentService.allDocCount(pageQuery);
        return ResultVO.ok(sysCodeCountVOList);
    }

    @ApiOperation("本周热点内容")
    @GetMapping("/hotContent/weekly")
    public ResultVO<PageVO<HotContentVO>> weeklyHotContent(PageQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        PageVO<HotContentVO> pageVO = hotContentService.weeklyHotContent(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("猜你关注")
    @PostMapping("/hotContent/guessYouLike")
    public ResultVO<PageVO<HotContentVO>> guessYouLike(@RequestBody GuessYouLikeQueryDTO pageQuery) throws Exception {
        pageQuery.paramReasonable();
        if (UserUtil.isLogin()) {
            pageQuery.setUserId(UserUtil.getCurrentUserId());
        }
        PageVO<HotContentVO> pageVO = hotContentService.guessYouLike(pageQuery);
        return ResultVO.ok(pageVO);
    }
}

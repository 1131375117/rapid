package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 热点问答接口
 *
 * @author wuxin
 */
@Api(tags = "热点问答")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class FrequentlyAskedQuestionController {

    private final FrequentlyAskedQuestionService frequentlyAskedQuestionService;

    @ApiOperation("热点问答列表查询")
    @PostMapping(value = "/getFrequentlyAskedQuestionList/query")
    public ResultVO<PageVO<PoliciesExplainDetailVO>> getFrequentlyAskedQuestionList(@RequestBody QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
        PageVO<PoliciesExplainDetailVO> frequentlyAskedQuestionPageVO = frequentlyAskedQuestionService.getFrequentlyAskedQuestionList(queryPoliciesExplainDTO);
        return ResultVO.ok(frequentlyAskedQuestionPageVO);
    }

    /**
     * 新增热门问答
     *
     * @param frequentlyAskedQuestionDTO
     * @return
     */
    @ApiOperation("新增热点解读")
    @PostMapping(value = "/frequentlyAskedQuestion")
    public ResultVO<Void> insertFrequentlyAskedQuestion(@RequestBody FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {

        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(frequentlyAskedQuestionDTO, UserUtil.getCurrentUser().getId());
        return ResultVO.ok();
    }


    /**
     * 修改热点问答
     *
     * @param frequentlyAskedQuestionDTO
     * @return
     */
    @ApiOperation("修改热点问答")
    @PutMapping(value = "frequentlyAskedQuestion-")
    public ResultVO<Void> updateFrequentlyAskedQuestion(@RequestBody FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO) {
        frequentlyAskedQuestionService.updateFrequentlyAskedQuestion(frequentlyAskedQuestionDTO);
        return ResultVO.ok();

    }
}

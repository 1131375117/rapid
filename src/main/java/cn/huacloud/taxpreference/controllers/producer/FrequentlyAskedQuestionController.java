package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 热点问答接口
 * @author wuxin
 */
@Api(tags = "热点问答")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class FrequentlyAskedQuestionController {

    private final FrequentlyAskedQuestionService frequentlyAskedQuestionService;


    /**
     * 新增热门问答
     * @param frequentlyAskedQuestionDTO
     * @return
     */
    @ApiOperation("新增热点解读")
    @PostMapping("FrequentlyAskedQuestion")
    public ResultVO<Void> insertFrequentlyAskedQuestion(@RequestBody FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO){

        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(frequentlyAskedQuestionDTO,UserUtil.getCurrentUser().getId());
        return ResultVO.ok();
    }
}

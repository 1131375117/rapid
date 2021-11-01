package cn.huacloud.taxpreference.controllers.producer;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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

    @PermissionInfo(name = "热点问答列表查询", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
    @SaCheckPermission("producer_frequentlyAskedQuestion_query")
    @ApiOperation("热点问答列表查询")
    @PostMapping(value = "/getFrequentlyAskedQuestionList/query")
    public ResultVO<PageVO<PoliciesExplainDetailVO>> getFrequentlyAskedQuestionList(@RequestBody QueryPoliciesExplainDTO queryPoliciesExplainDTO) {
        PageVO<PoliciesExplainDetailVO> frequentlyAskedQuestionPageVO = frequentlyAskedQuestionService.getFrequentlyAskedQuestionList(queryPoliciesExplainDTO);
        return ResultVO.ok(frequentlyAskedQuestionPageVO);
    }

    /**
     * 新增热门问答
     *
     * @param frequentlyAskedQuestionDTOS
     * @return
     */
    @PermissionInfo(name = "新增热点问答", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
    @SaCheckPermission("producer_frequentlyAskedQuestion_insert")
    @ApiOperation("新增热点问答")
    @PostMapping(value = "/frequentlyAskedQuestion")
    public ResultVO<Void> insertFrequentlyAskedQuestion(@Validated(ValidationGroup.Create.class)@RequestBody List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOS) {

        frequentlyAskedQuestionService.insertFrequentlyAskedQuestion(frequentlyAskedQuestionDTOS, UserUtil.getCurrentUser().getId());
        return ResultVO.ok();
    }


    /**
     * 修改热点问答
     *
     * @param frequentlyAskedQuestionDTOS
     * @return
     */
    @PermissionInfo(name = "修改热点问答", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
    @SaCheckPermission("producer_frequentlyAskedQuestion_update")
    @ApiOperation("修改热点问答")
    @PutMapping(value = "/frequentlyAskedQuestion/update")
    public ResultVO<Void> updateFrequentlyAskedQuestion(@Validated(ValidationGroup.Update.class)@RequestBody List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOS) {
        frequentlyAskedQuestionService.updateFrequentlyAskedQuestion(frequentlyAskedQuestionDTOS);
        return ResultVO.ok();

    }

    @PermissionInfo(name = "删除热点问答", group = PermissionGroup.FREQUENTLY_ASKED_QUESTION)
    @SaCheckPermission("producer_frequentlyAskedQuestion_delete")
    @ApiOperation("删除热点问答")
    @DeleteMapping(value = "/frequentlyAskedQuestion/delete")
    public ResultVO<Void> deleteFrequentlyAskedQuestion(@Validated @NotEmpty(message = "id不能为空")@PathVariable("id") Long id){
        frequentlyAskedQuestionService.deleteFrequentlyAskedQuestion(id);
        return ResultVO.ok();
    }
}

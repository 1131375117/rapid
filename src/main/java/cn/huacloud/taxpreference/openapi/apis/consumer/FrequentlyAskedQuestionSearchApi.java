package cn.huacloud.taxpreference.openapi.apis.consumer;

import cn.huacloud.taxpreference.common.annotations.LimitApi;
import cn.huacloud.taxpreference.common.annotations.MonitorInterface;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.services.consumer.FrequentlyAskedQuestionSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.FAQSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.OrganizationVO;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangkh
 */
@ApiSupport(order = 700)
@Api(tags = "热点问答检索")
@RequiredArgsConstructor
@RequestMapping("/open-api/v1/search")
@RestController
public class FrequentlyAskedQuestionSearchApi {

    private final FrequentlyAskedQuestionSearchService frequentlyAskedQuestionSearchService;

    @ApiOperation("热门问答简单列表")
    @OpenApiCheckToken
    @MonitorInterface
    @LimitApi
    @GetMapping("/faq/hot")
    public ResultVO<PageVO<FAQSearchSimpleVO>> hotFAQList(PageQueryDTO pageQuery) throws Exception {
        PageVO<FAQSearchSimpleVO> pageVO = frequentlyAskedQuestionSearchService.hotFAQList(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("热点问答搜索")
    @PostMapping("/faq")
    @OpenApiCheckToken
    @MonitorInterface
    @LimitApi
    public ResultVO<PageVO<FAQSearchVO>> pageSearch(@RequestBody FAQSearchQueryDTO pageQuery) throws Exception {
        PageVO<FAQSearchVO> pageVO = frequentlyAskedQuestionSearchService.pageSearch(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("获取所有热门问答来源")
    @GetMapping("/faq/answerOrganization")
    @OpenApiCheckToken
    @MonitorInterface
    @LimitApi
    public ResultVO<List<OrganizationVO>> getFaqAnswerOrganization(@RequestParam(value = "size", defaultValue = "200") Integer size) throws Exception {

        List<OrganizationVO> answerOrganizationList = frequentlyAskedQuestionSearchService.getFaqAnswerOrganization(size);
        return ResultVO.ok(answerOrganizationList);
    }

    @ApiOperation("获取所有主题分类")
    @GetMapping("/faq/subjectType")
    @OpenApiCheckToken
    @MonitorInterface
    @LimitApi
    public ResultVO<List<String>> getFaqSubjectType(@RequestParam(value = "size", defaultValue = "200") Integer size) throws Exception {

        List<String> subjectTypeList = frequentlyAskedQuestionSearchService.getFaqSubjectType(size);
        return ResultVO.ok(subjectTypeList);
    }

    @ApiOperation("热点问答详情")
    @GetMapping("/faq/{id}")
    @OpenApiCheckToken
    @MonitorInterface
    @LimitApi
    public ResultVO<FAQSearchVO> getFAQDetails(@PathVariable(value = "id") @ApiParam(example = "8753") Long id) throws Exception {
        FAQSearchVO faqSearchVO = frequentlyAskedQuestionSearchService.getFAQDetails(id);
        faqSearchVO.initUserCollectionInfo(CollectionType.FREQUENTLY_ASKED_QUESTION);
        return ResultVO.ok(faqSearchVO);
    }



    @ApiOperation("根据政策ID查询相关热点问答")
    @GetMapping("/faq/policiesRelated/{policiesId}")
    @OpenApiCheckToken
    @MonitorInterface
    @LimitApi
    public ResultVO<PageVO<FAQSearchVO>> policiesRelatedFAQ(@PathVariable("policiesId") @ApiParam(example = "8753") Long policiesId, PageQueryDTO pageQuery) throws Exception {
        PageVO<FAQSearchVO> pageVO = frequentlyAskedQuestionSearchService.policiesRelatedFAQ(policiesId, pageQuery);
        return ResultVO.ok(pageVO);
    }
}

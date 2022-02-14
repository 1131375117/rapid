package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.FrequentlyAskedQuestionSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.FAQSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.OrganizationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangkh
 */
@Api(tags = "热点问答检索")
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class FrequentlyAskedQuestionSearchController {

    private final FrequentlyAskedQuestionSearchService frequentlyAskedQuestionSearchService;

    @ApiOperation("热门问答简单列表")
    @GetMapping("/faq/hot")
    public ResultVO<PageVO<FAQSearchSimpleVO>> hotFAQList(PageQueryDTO pageQuery) throws Exception {
        PageVO<FAQSearchSimpleVO> pageVO = frequentlyAskedQuestionSearchService.hotFAQList(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("热点问答搜索")
    @PostMapping("/faq")
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<FAQSearchVO>> pageSearch(@RequestBody FAQSearchQueryDTO pageQuery) throws Exception {
        PageVO<FAQSearchVO> pageVO = frequentlyAskedQuestionSearchService.pageSearch(pageQuery);
        return ResultVO.ok(pageVO);
    }

    @ApiOperation("获取所有热门问答来源")
    @GetMapping("/faq/answerOrganization")
    @ConsumerUserCheckLogin
    public ResultVO<List<OrganizationVO>> getFaqAnswerOrganization(@RequestParam(value = "size", defaultValue = "200") Integer size) throws Exception {

        List<OrganizationVO> answerOrganizationList = frequentlyAskedQuestionSearchService.getFaqAnswerOrganization(size);
        return ResultVO.ok(answerOrganizationList);
    }

    @ApiOperation("获取所有主题分类")
    @GetMapping("/faq/subjectType")
    @ConsumerUserCheckLogin
    public ResultVO<List<String>> getFaqSubjectType(@RequestParam(value = "size", defaultValue = "200") Integer size) throws Exception {

        List<String> subjectTypeList = frequentlyAskedQuestionSearchService.getFaqSubjectType(size);
        return ResultVO.ok(subjectTypeList);
    }

    @ApiOperation("热点问答详情")
    @GetMapping("/faq/{id}")
    @ConsumerUserCheckLogin
    public ResultVO<FAQSearchVO> getFAQDetails(@PathVariable("id") Long id) throws Exception {
        FAQSearchVO faqSearchVO = frequentlyAskedQuestionSearchService.getFAQDetails(id);
        faqSearchVO.initUserCollectionInfo(CollectionType.FREQUENTLY_ASKED_QUESTION);
        return ResultVO.ok(faqSearchVO);
    }



    @ApiOperation("根据政策ID查询相关热点问答")
    @GetMapping("/faq/policiesRelated/{policiesId}")
    @ConsumerUserCheckLogin
    public ResultVO<PageVO<FAQSearchVO>> policiesRelatedFAQ(@PathVariable("policiesId") Long policiesId, PageQueryDTO pageQuery) throws Exception {
        PageVO<FAQSearchVO> pageVO = frequentlyAskedQuestionSearchService.policiesRelatedFAQ(policiesId, pageQuery);
        return ResultVO.ok(pageVO);
    }
}

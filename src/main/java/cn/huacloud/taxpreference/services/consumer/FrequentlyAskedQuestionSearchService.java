package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.FAQSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchVO;

import java.util.List;

/**
 * 热点问答检索服务
 * @author wangkh
 */
public interface FrequentlyAskedQuestionSearchService extends SearchService<FAQSearchQueryDTO, FAQSearchVO> {

    /**
     * 热门问答简单列表
     * @param pageQuery 分页查询条件
     * @return 分页数据
     */
    PageVO<FAQSearchSimpleVO> hotFAQList(PageQueryDTO pageQuery) throws Exception;

    /**
     * 热点问答详情
     * @param id 热门问答ID
     * @return 热门问答视图
     */
    FAQSearchVO getFAQDetails(Long id) throws Exception;

    /**
     * 根据政策ID查询相关热点问答
     * @param policiesId 政策法规ID
     * @param pageQuery 分页参数
     * @return 分页数据
     */
    PageVO<FAQSearchVO> policiesRelatedFAQ(Long policiesId, PageQueryDTO pageQuery) throws Exception;

    /**
     * 获取所有热门问答来源
     * @param size 一次获取多少数据
     * @return docSourceList
     */
    List<String> getFaqDocSource(Integer size) throws Exception;
}

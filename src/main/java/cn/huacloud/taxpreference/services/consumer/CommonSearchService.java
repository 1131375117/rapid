package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeCountVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.GuessYouLikeQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotContentVO;

import java.util.List;

/**
 * 热点内容服务
 * @author wangkh
 */
public interface CommonSearchService {
    /**
     * 本周热点内容
     * 热点内容通过浏览次数、收藏次数来
     * @param pageQuery 分页查询对象
     * @return 本周热点内容
     */
    PageVO<HotContentVO> weeklyHotContent(PageQueryDTO pageQuery) throws Exception;

    /**
     * 猜你关注
     * @param pageQuery 分页查询对象
     * @return 推荐内容
     */
    PageVO<HotContentVO> guessYouLike(GuessYouLikeQueryDTO pageQuery) throws Exception;

    /**
     * 高级搜索统计所有文档数
     * @param pageQuery 查询条件
     * @return 文档类型统计数
     */
    List<SysCodeCountVO> allDocCount(KeywordPageQueryDTO pageQuery) throws Exception;
}

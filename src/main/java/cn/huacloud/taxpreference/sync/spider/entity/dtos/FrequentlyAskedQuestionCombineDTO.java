package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhaoqiankun
 * @date 2021/12/29
 *         tax_preference_dev库 热门回答联合DTO
 */
@Accessors(chain = true)
@Data
public class FrequentlyAskedQuestionCombineDTO {

    /**
     * ax_preference_dev库 热门回答 对象
     */
    private FrequentlyAskedQuestionDO frequentlyAskedQuestionDO;

}

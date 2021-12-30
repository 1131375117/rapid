package cn.huacloud.taxpreference.sync.spider.entity.dos;

import lombok.Data;

/**
 * @author wangkh
 */
@Data
public class SpiderPolicyPopularDataDO {

    /**
     * 自增主键id
     */
    private Integer id;
    /**
     * 政策表-id
     */
    private String policyId;
    /**
     * 热门问答表-id
     */
    private String popularQaId;

}

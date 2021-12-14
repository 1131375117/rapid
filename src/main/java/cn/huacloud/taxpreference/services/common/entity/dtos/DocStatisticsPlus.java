package cn.huacloud.taxpreference.services.common.entity.dtos;

import cn.huacloud.taxpreference.common.enums.DocType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class DocStatisticsPlus {

    /**
     * 文档类型
     * */
    private DocType docType;

    /**
     * 文档id
     * */
    private Long docId;

    /**
     * 浏览量
     * */
    private Long viewsPlus;

    /**
     * 收藏数
     * */
    private Long collectionsPlus;
}

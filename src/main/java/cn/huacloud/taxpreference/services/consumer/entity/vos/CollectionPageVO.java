package cn.huacloud.taxpreference.services.consumer.entity.vos;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class CollectionPageVO<T> {
    /**
     * 日期
     */
    private LocalDate date;
    /**
     * 分页数据
     */
    private List<T> pageVOList;
}

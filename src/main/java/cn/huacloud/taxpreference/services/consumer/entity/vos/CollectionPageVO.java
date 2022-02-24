package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("日期")
    private LocalDate date;
    /**
     * 分页数据
     */
    @ApiModelProperty("最终所需查询收藏集合")
    private List<T> pageVOList;

}

package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class OperationPageVO<T> {
    /**
     * 日期
     */
    @ApiModelProperty("日期")
    private String date;
    /**
     * 分页数据
     */
    @ApiModelProperty("具体数据")
    private List<T> pageVOList;
    /**
     * 是否是当今日期
     */
    @ApiModelProperty("是否是今天(已被date字段代替)")
    private Boolean isToday;
}

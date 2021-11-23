package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上一篇、下一篇视图
 *
 * @author wangkh
 */
@Data
public class PreviousNextVO<T> {
    @ApiModelProperty("上一篇")
    private Doc<T> previous;
    @ApiModelProperty("下一篇")
    private Doc<T> next;

    @Data
    public static class Doc<T> {
        @ApiModelProperty("ID主键")
        private T id;
        @ApiModelProperty("标题")
        private String title;
    }
}

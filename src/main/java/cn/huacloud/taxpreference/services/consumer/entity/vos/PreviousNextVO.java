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
    @ApiModelProperty(value = "上一篇",example = "49307")
    private Doc<T> previous;
    @ApiModelProperty(value = "下一篇",example = "49309")
    private Doc<T> next;

    @Data
    public static class Doc<T> {
        @ApiModelProperty("ID主键")
        private T id;
        @ApiModelProperty(value = "标题",example = "关于取消增值税一般纳税人年检收费的通知")
        private String title;
    }
}

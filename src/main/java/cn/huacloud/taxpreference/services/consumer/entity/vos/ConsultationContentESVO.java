package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.enums.ContentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhengkai.blog.csdn.net
 * @description 热门咨询contentVO
 * @date 2022-02-17
 */
@Data
public class ConsultationContentESVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("咨询内容类型(question,answer)")
    private ContentType contentType;

    /**
     * 文本内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 咨询图片
     */
    @ApiModelProperty("图片")
    private List<String> imageUris;

    @ApiModelProperty("咨询时间")
    private LocalDateTime createTime;

}
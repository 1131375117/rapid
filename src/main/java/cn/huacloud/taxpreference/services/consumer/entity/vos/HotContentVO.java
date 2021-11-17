package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 热点内容
 * @author wangkh
 */
@Data
public class HotContentVO {
    @ApiModelProperty("ID主键")
    private Long id;

    @ApiModelProperty("内容类型")
    private SysCodeSimpleVO hotContentType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;

    @ApiModelProperty("标签")
    private List<String> labels;
}

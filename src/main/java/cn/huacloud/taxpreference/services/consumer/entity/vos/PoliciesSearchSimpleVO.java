package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wangkh
 */
@Data
public class PoliciesSearchSimpleVO {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("所属区域")
    private SysCodeSimpleVO area;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;
    @ApiModelProperty("文档来源")
    private String docSource;
    @ApiModelProperty("税种")
    private List<SysCodeSimpleVO> taxCategories;

}

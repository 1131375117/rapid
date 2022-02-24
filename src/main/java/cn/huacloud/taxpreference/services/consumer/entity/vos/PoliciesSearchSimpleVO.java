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
    @ApiModelProperty(value = "主键ID",example = "49308")
    private Long id;

    @ApiModelProperty(value = "标题",example = "国家税务总局 财政部关于制造业中小微企业延缓缴纳2021年第四季度部分税费有关事项的公告")
    private String title;

    @ApiModelProperty("所属区域")
    private SysCodeSimpleVO area;

    @ApiModelProperty(value = "发布日期",example = "2021-10-29")
    private LocalDate releaseDate;
    @ApiModelProperty(value = "文档来源",example = "国家税务总局,财政部")
    private String docSource;
    @ApiModelProperty("税种")
    private List<SysCodeSimpleVO> taxCategories;

}

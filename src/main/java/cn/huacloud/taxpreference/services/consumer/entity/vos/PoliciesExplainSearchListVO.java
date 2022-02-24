package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 政策解读检索列表视图
 *
 * @author wangkh
 */
@Data
public class PoliciesExplainSearchListVO {
    @ApiModelProperty(value = "主键ID", example = "3007")
    private Long id;

    @ApiModelProperty(value = "标题", example = "222222")
    private String title;

    @ApiModelProperty("所属区域")
    private SysCodeSimpleVO area;

    @ApiModelProperty(value = "来源", example = "文档来源")
    private String docSource;

    @ApiModelProperty("所属税种")
    private List<SysCodeSimpleVO> taxCategories;

    @ApiModelProperty("纳税人资格认定类型")
    private List<SysCodeSimpleVO> taxpayerIdentifyTypes;

    @ApiModelProperty("适用企业类型名称")
    private List<SysCodeSimpleVO> enterpriseTypes;

    @ApiModelProperty("适用行业名称")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty(value = "发布日期", example = "2022-01-13")
    private LocalDate releaseDate;

    @ApiModelProperty(value = "正文", example = "这是正文部分")
    private String content;

    @ApiModelProperty(value = "无装饰的组合文本", example = "日，国家税务总局公布了《重大税收违法失信主体信息公布管理办法》（国家税务总局令第54号），现就有关事项解读如下： 一、修订背景 为贯彻中共中央办公厅、国务院办公厅《关于进一步深化税收征管改革的意见》和《国务院办公厅关于进一步完善失信约束制度构建诚信建设长效机制的指导意见》（国办发〔2020〕49号），深入推进重大税收违法失信案件管理工作，规范管理流程")
    private String combinePlainContent;
}

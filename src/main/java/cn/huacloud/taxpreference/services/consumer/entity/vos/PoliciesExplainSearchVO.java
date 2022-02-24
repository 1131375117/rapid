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
public class PoliciesExplainSearchVO extends UserCollectionInfo {
    @ApiModelProperty(value = "主键ID", example = "2360")
    private Long id;

    @ApiModelProperty(value = "标题", example = "关于《国家税务总局 财政部关于制造业中小微企业延缓缴纳2021年第四季度部分税费有关事项的公告》的解读")
    private String title;

    @ApiModelProperty("所属区域")
    private SysCodeSimpleVO area;

    @ApiModelProperty(value = "来源", example = "国家税务总局办公厅")
    private String docSource;

    @ApiModelProperty("所属税种")
    private List<SysCodeSimpleVO> taxCategories;

    @ApiModelProperty("纳税人资格认定类型")
    private List<SysCodeSimpleVO> taxpayerIdentifyTypes;

    @ApiModelProperty("适用企业类型名称")
    private List<SysCodeSimpleVO> enterpriseTypes;

    @ApiModelProperty("适用行业名称")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty(value = "发布日期", example = "2021-10-29")
    private LocalDate releaseDate;

    @ApiModelProperty(value = "正文", example = "\\\"font-weight: bold;\\\">一、《公告》是在什么背景下出台的？</span></p> \\n  <p align=\\\"\\\" style=\\\"text-indent: 2em; text-align: justify;\\\">为贯彻落实党中央、国务院决策部署，支持制造业中小微企业发展，促进工业经济平稳运行，稳定市场预期和就业，税务总局联合财政部出台本《公告》，明确")
    private String content;

    @ApiModelProperty("上一篇、下一篇")
    PreviousNextVO<Long> previousNext;

    @ApiModelProperty(value = "浏览量", example = "0")
    private Long views;

    @ApiModelProperty(value = "收藏量", example = "10")
    private Long collections;

    @ApiModelProperty(value = "被解读政策")
    private DocSimpleVO policies;
}

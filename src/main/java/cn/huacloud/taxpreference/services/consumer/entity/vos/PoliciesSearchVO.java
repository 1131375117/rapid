package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 政策法规检索详情视图
 * @author wangkh
 */
@Data
public class PoliciesSearchVO extends UserCollectionInfo {

    @ApiModelProperty(value = "主键ID",example = "49308")
    private Long id;

    @ApiModelProperty(value = "标题",example = "国家税务总局 财政部关于制造业中小微企业延缓缴纳202")
    private String title;

    @ApiModelProperty(value = "文号",example = "国家税务总局公告〔2021〕30号")
    private String docCode;

    @ApiModelProperty("所属区域")
    private SysCodeSimpleVO area;

    @ApiModelProperty(value = "来源",example = "国家税务总局,财政部")
    private String docSource;

    @ApiModelProperty("所属税种")
    private List<SysCodeSimpleVO> taxCategories;

    @ApiModelProperty("纳税人资格认定类型")
    private List<SysCodeSimpleVO> taxpayerIdentifyTypes;

    @ApiModelProperty("适用企业类型名称")
    private List<SysCodeSimpleVO> enterpriseTypes;

    @ApiModelProperty("适用行业名称")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty(value = "有效性",example = "FULL_TEXT_VALID")
    private SysCodeSimpleVO validity;

    @ApiModelProperty(value = "发布日期",example = "2021-10-29")
    private LocalDate releaseDate;

    @ApiModelProperty(value = "摘要",example = "注释： 为贯彻落实党中央、国务院决策")
    private String digest;

    @ApiModelProperty(value = "正文",example = "-align: justify;\\\">为贯彻落实党中央、国务院决策部署，支持制造业中小微企业发展，促进工业经济平稳运行，现就制造业中小微企业（含个人独资企业、合伙企业、个体工商户，下同）延缓缴纳2021年第四季度部分税费有关事项公告如下：</p> \\n  <p align=\\\"\\\" style=\\\"text-indent: 2em; text-align: justify;\\")
    private String content;

    @ApiModelProperty("标签集合")
    private List<String> labels;

    @ApiModelProperty(value = "废止说明")
    private String abolishNote;

    @ApiModelProperty("上一篇、下一篇")
    PreviousNextVO<Long> previousNext;

    @ApiModelProperty(value = "浏览量",example = "1")
    private Long views;

    @ApiModelProperty(value = "收藏量",example = "10")
    private Long collections;
}

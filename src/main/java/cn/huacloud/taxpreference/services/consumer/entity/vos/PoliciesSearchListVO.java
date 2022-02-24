package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 政策法规检索列表视图
 * @author wangkh
 */
@Data
public class PoliciesSearchListVO {

    @ApiModelProperty(value = "主键ID",example = "49308")
    private Long id;

    @ApiModelProperty(value = "标题",example = "国家税务总局 财政部关于制造业中小微企业延缓缴纳2021年第四季度部分税费有关事项的公告")
    private String title;

    @ApiModelProperty(value = "文号",example = "国家税务总局公告〔2021〕30号")
    private String docCode;

    @ApiModelProperty(value = "所属区域")
    private SysCodeSimpleVO area;

    @ApiModelProperty(value = "来源",example = "国家税务总局,财政部")
    private String docSource;

    @ApiModelProperty(value = "所属税种")
    private List<SysCodeSimpleVO> taxCategories;

    @ApiModelProperty(value = "纳税人资格认定类型")
    private List<SysCodeSimpleVO> taxpayerIdentifyTypes;

    @ApiModelProperty(value = "适用企业类型名称")
    private List<SysCodeSimpleVO> enterpriseTypes;

    @ApiModelProperty(value = "适用行业名称")
    private List<SysCodeSimpleVO> industries;

    @ApiModelProperty(value = "有效性")
    private SysCodeSimpleVO validity;

    @ApiModelProperty(value = "发布日期",example = "2021-10-29")
    private LocalDate releaseDate;

    @ApiModelProperty(value = "摘要",example = "注释： 为贯彻落实党")
    private String digest;

    @ApiModelProperty(value = "标签集合")
    private List<String> labels;

    @ApiModelProperty(value = "无装饰的组合文本",example = "注释： 为贯彻落实党中央、国务院决策部署，支持制造业中小微企业发展，促进工业经济平稳运行，现就制造业中小微企业（含个人独资企业、合伙企业、个体工商户，下同）延缓缴纳2021年第四季度部分税费有关事项公告如下： 一、本公告所称制造业中小微企业是指国民经济行业分类中行业门类为制造业，且年销售额2000万元以上（含2000万元）4亿元以下（不含4亿元）的企业（以下称制造业中型企业）和年销售额2000万元 注释： 为贯彻落实党中央、国务院决策部署，支持制造业中小微企业发展，促进工业经济平稳运行，现就制造业中小微企业（含个人独资企业、合伙企业、个体工商户，下同）延缓缴纳2021年第四季度部分税费有关事项公告如下： 一、本公告所称制造业中小微企业")
    private String combinePlainContent;

    @ApiModelProperty(value = "浏览量",example = "0")
    private Long views;

    @ApiModelProperty(value = "收藏量",example = "10")
    private Long collections;
}

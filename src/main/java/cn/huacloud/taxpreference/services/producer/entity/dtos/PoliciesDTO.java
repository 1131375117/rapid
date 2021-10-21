package cn.huacloud.taxpreference.services.producer.entity.dtos;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author wuxin
 */
@Data
@TableName("t_policies")
public class PoliciesDTO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文号
     */
    private String docCode;

    /**
     * 所属区域名称
     */
    private String areaName;

    /**
     * 所属区域码值
     */
    private String areaCode;

    /**
     * 来源
     */
    private String docSource;

    /**
     * 所属税种名称
     */
    private String taxCategoriesName;

    /**
     * 所属税种码值
     */
    private String taxCategoriesCode;

    /**
     * 纳税人资格认定类型名称
     */
    private String taxpayerIdentifyTypeName;

    /**
     * 纳税人资格认定类型码值
     */
    private String taxpayerIdentifyTypeCode;

    /**
     * 适用企业类型名称
     */
    private String enterpriseTypeName;

    /**
     * 适用企业类型码值
     */
    private String enterpriseTypeCode;

    /**
     * 适用行业名称
     */
    private String industryName;

    /**
     * 适用行业码值
     */
    private String industryCode;

    /**
     * 有效性
     */
    private String validity;

    /**
     * 发布日期
     */
    private Date releaseDate;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 正文
     */
    private String content;

}

package cn.huacloud.taxpreference.services.producer.entity.dos;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 政策法规实体
 *
 * @author wuxin
 */
@Data
@TableName("t_policies")
public class PoliciesDO {

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
    private LocalDate releaseDate;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 正文
     */
    private String content;

    /**
     * 标签集合
     */
    private String labels;

    /**
     * 政策法规状态
     */
    private String policiesStatus;

    /**
     * 废止说明
     */
    private String abolishNote;

    /**
     * 录入人用户ID
     */
    private Long inputUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;


}

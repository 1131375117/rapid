package cn.huacloud.taxpreference.services.producer.entity.dos;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: 税收优惠实体
 * @author: fuhua
 * @create: 2021-10-21 09:32
 **/
@Data
@TableName("t_tax_preference")
public class TaxPreferenceDO {
    /**
     * 主键自增
     */
    @TableId(type=IdType.AUTO)
    private Long id;

    /**
     * 收入税种种类名称
     */
    private String taxCategoriesName;

    /**
     * 收入税种种类码值
     */
    private String taxCategoriesCode;

    /**
     * 纳税人登记注册类型名称
     */
    private String taxpayerRegisterTypeName;

    /**
     * 纳税人登记注册类型码值
     */
    private String taxpayerRegisterTypeCode;

    /**
     * 纳税人类型名称
     */
    private String taxpayerTypeName;

    /**
     * 纳税人类型码值
     */
    private String taxpayerTypeCode;

    /**
     * 适用行业名称
     */
    private String industryName;

    /**
     * 适用行业码值
     */
    private String industryCode;

    /**
     * 适用企业类型名称
     */
    private String enterpriseTypeName;

    /**
     * 适用企业类型码值
     */
    private String enterpriseTypeCode;

    /**
     * 纳税信用等级
     */
    private String taxpayerCreditRating;

    /**
     * 优惠事项名称
     */
    private String taxPreferenceName;

    /**
     * 具体优惠内容摘要
     */
    private String digest;

    /**
     * 留存备查资料
     */
    private String keepQueryData;

    /**
     * 提交税务机关资料
     */
    private String submitTaxData;

    /**
     * 资料报送时限
     */
    private String submitTimeLimit;

    /**
     * 申报表填写简要说明
     */
    private String submitDescription;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    private Date updateTime;

    /**
     * 是否已删除
     */
    private Integer deleted;


}
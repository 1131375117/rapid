package cn.huacloud.taxpreference.services.producer.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description: 税收优惠政策法规关联表实体
 * @author: fuhua
 * @create: 2021-10-21 09:59
 **/
@Data
@TableName("t_tax_preference_policies")
public class TaxPreferencePoliciesDO {

    /**
     * 主键自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 税收优惠ID
     */
    private Long taxPreferenceId;

    /**
     * 政策法规ID
     */
    private Long policiesId;

    /**
     * 有效期起
     */
    private String validityBeginDate;

    /**
     * 有效期至
     */
    private String validityEndDate;

    /**
     * 排序字段
     */
    private Long sort;

    /**
     * 具体优惠内容摘要
     */
    private String digest;

    /**
     * 具体条款
     */
    private String policiesItems;
    /**
     * 政策法规日期类型
     */
    private Boolean dateType;


}

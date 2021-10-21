package cn.huacloud.taxpreference.services.producer.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 政策解读实体
 * @author wuxin
 */
@Data
@TableName("t_policies_explain")
public class PoliciesExplainDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 政策ID
     */
    private Long policiesId;

    /**
     * 标题
     */
    private String title;

    /**
     * 来源
     */
    private String docSource;

    /**
     * 发布日期
     */
    private Date releaseDate;

    /**
     * 正文
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

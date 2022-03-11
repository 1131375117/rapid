package cn.huacloud.taxpreference.services.producer.entity.dos;

import cn.huacloud.taxpreference.common.enums.ReplyStatus;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description 热门咨询表
 * @author fuhua
 * @date 2022-02-17
 */
@Data
@TableName("t_consultation")
@Accessors(chain = true)
public class ConsultationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
    * id
    */
    private Long id;

    /**
    * 所属税种码值
    */
    private String taxCategoriesCodes;

    /**
    * 所属税种名称
    */
    private String taxCategoriesNames;

    /**
    * 所属税务实务名称
    */
    private String taxPractices;

    /**
    * 适用行业码值
    */
    private String industryCodes;

    /**
    * 适用行业名称
    */
    private String industryNames;

    /**
    * 客户用户id
    */
    private Long customerUserId;

    /**
    * 专家用户id
    */
    private Long professorUserId;

    /**
    * 解答日期
    */
    private LocalDateTime finishTime;

    /**
    * 咨询状态（not_reply-已答复，rnhave_reply）
    */
    private ReplyStatus status;

    /**
    * create_time
    */
    private LocalDateTime createTime;

    /**
     * 是否公开
     */
    private Long published;

    /**
     * 手机号
     */
    private Long phoneNumber;

}
package cn.huacloud.taxpreference.services.wework.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
@TableName("t_wwx_company")
public class WeWorkCompanyDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String corpId;
    private String permanentCode;
    private String corpName;
    private String corpFullName;
    private String subjectType;
    private String verifiedEndTime;
    private String suiteId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean deleted;
}

package cn.huacloud.taxpreference.services.wwx.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Data
@TableName("t_wwx_third_company")
public class WWXThirdCompanyDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    @JsonProperty(namespace = "auth_corp_info", value = "corpid")
    private String corpId;
    @JsonProperty("permanent_code")
    private String permanentCode;
    @JsonProperty("auth_corp_info.corp_name")
    private String corpName;
    private String corpFullName;
    private Integer subjectType;
    private String verifiedEndTime;
    private Integer agentId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

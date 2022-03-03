package cn.huacloud.taxpreference.services.message.entity.dtos;

import lombok.Data;

/**
 * @author fuhua
 **/
@Data
public class TencentSesParamDTO {
    // 密钥ID
    private String secretId;
    // 密钥Key
    private String secretKey;
    // 所属区域
    private String region;
    // 邮箱发送方
    private String fromEmailAddress;
    // 邮件模板ID
    private Long templateId;
    // 邮件主题
    private String subjectType;
}

package cn.huacloud.taxpreference.services.openapi.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Data
public class TokenInfoVO {
    @ApiModelProperty("授权token")
    private String token;
    @ApiModelProperty("失效时间")
    private LocalDateTime invalidTime;
}

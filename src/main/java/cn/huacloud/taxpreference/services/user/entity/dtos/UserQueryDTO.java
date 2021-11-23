package cn.huacloud.taxpreference.services.user.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangkh
 */
@Data
public class UserQueryDTO extends KeywordPageQueryDTO {
    @ApiModelProperty("用户账户光健字")
    private String userAccountKeyword;
    @ApiModelProperty("用户名称关键字")
    private String usernameKeyword;
    @ApiModelProperty("角色码值")
    private String roleCode;
    @ApiModelProperty("排除的角色码值")
    private String excludeRoleCode;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        // 空字符串设置为null，或者去掉前后空格
        stringParamNullOrTrim();
    }
}

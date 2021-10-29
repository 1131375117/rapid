package cn.huacloud.taxpreference.services.user.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.enums.UserType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        // 空字符串设置为null，或者去掉前后空格
        userAccountKeyword = StringUtils.isNoneBlank(userAccountKeyword) ? usernameKeyword.trim() : null;
        usernameKeyword = StringUtils.isNoneBlank(usernameKeyword) ? usernameKeyword.trim() : null;
        roleCode = StringUtils.isNoneBlank(roleCode) ? roleCode.trim() : null;
    }
}

package cn.huacloud.taxpreference.services.user.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.enums.user.UserType;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色查询
 * @author wangkh
 */
@Getter
@Setter
public class RoleQueryDTO extends PageQueryDTO {
    private UserType userType;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (userType == null) {
            userType = UserType.PRODUCER;
        }
    }
}

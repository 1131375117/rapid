package cn.huacloud.taxpreference.services.user.entity.vos;

import cn.huacloud.taxpreference.common.enums.UserType;
import lombok.Data;

import java.util.List;

/**
 *
 * @author wangkh
 */
@Data
public class LoginUserVO {
    private Long id;
    private String userAccount;
    private String username;
    private UserType userType;
    private List<String> roleCodes;
    private List<String> permission;
}

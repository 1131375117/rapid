package cn.huacloud.taxpreference.services.wework.entity.dtos;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.user.ChannelUserType;
import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.services.wework.support.ObjectMapperProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 第三方用户身份信息
 * @author wangkh
 */
@Data
public class UserInfo3rdDTO {
    private String userId;
    private String openUserId;
    private String openId;
    private String corpId;

    public String lookupOpenUserId() {
        List<Function<UserInfo3rdDTO, String>> functions = new ArrayList<>();
        functions.add(UserInfo3rdDTO::getOpenUserId);
        functions.add(UserInfo3rdDTO::getUserId);
        functions.add(UserInfo3rdDTO::getOpenId);
        for (Function<UserInfo3rdDTO, String> function : functions) {
            String userId = function.apply(this);
            if (userId != null) {
                return userId;
            }
        }
        String json = ObjectMapperProvider.writeJsonPrettyString(this);
        throw new RuntimeException("找不到有效的userId: \n" + json);
    }

    public ChannelUserType lookupChannelUserType() {
        if (userId != null || openUserId != null) {
            return ChannelUserType.WORK_WEI_XIN;
        }
        if (openId != null) {
            return ChannelUserType.WEI_XIN;
        }
        throw new RuntimeException("获取第三方用户渠道类型失败，所有ID均为null");
    }
}

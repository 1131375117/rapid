package cn.huacloud.taxpreference.services.wework.entity.dtos;

import cn.huacloud.taxpreference.services.wework.support.ObjectMapperProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author wangkh
 */
@Data
public class UserInfo3rdDTO {
    private String userId;
    private String openUserId;
    private String openId;

    @JsonIgnore
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
}

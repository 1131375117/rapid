package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.BaseApplicationTest;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceTest extends BaseApplicationTest {

    @Autowired
    UserService userService;

    @Test
    public void userList() {
        List<UserDO> userDOS = userService.userList();
        Assert.assertFalse(userDOS.isEmpty());
    }
}
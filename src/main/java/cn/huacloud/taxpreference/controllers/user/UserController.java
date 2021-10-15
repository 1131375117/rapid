package cn.huacloud.taxpreference.controllers.user;

import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户前端控制器
 * @author wangkh
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/user-list")
    public List<UserDO> userDOList() {
        return userService.userList();
    }
}

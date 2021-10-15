package cn.huacloud.taxpreference.services.user.impl;

import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import cn.huacloud.taxpreference.services.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public List<UserDO> userList() {
        return userMapper.selectList(null);
    }
}

package cn.huacloud.taxpreference.services.user.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.user.CreateWay;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.user.ConsumerUserService;
import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import cn.huacloud.taxpreference.services.user.entity.vos.ConsumerLoginUserVO;
import cn.huacloud.taxpreference.services.user.mapper.ConsumerUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class ConsumerUserServiceImpl implements ConsumerUserService {

    private final ConsumerUserMapper consumerUserMapper;

    @Override
    public ConsumerLoginUserVO getLoginUserVOWithPassword(String account) {
        LambdaQueryWrapper<ConsumerUserDO> queryWrapper = Wrappers.lambdaQuery(ConsumerUserDO.class)
                .and(i -> i.eq(ConsumerUserDO::getUserAccount, account)
                        .or().eq(ConsumerUserDO::getPhoneNumber, account)
                        .or().eq(ConsumerUserDO::getEmail, account))
                .eq(ConsumerUserDO::getDeleted, false);

        ConsumerUserDO consumerUserDO = consumerUserMapper.selectOne(queryWrapper);
        return CustomBeanUtil.copyProperties(consumerUserDO, ConsumerLoginUserVO.class);
    }

    @Transactional
    @Override
    public void autoCreateUserByPhoneNumber(String phoneNumber) {
        if (consumerUserMapper.isPhoneNumberRegister(phoneNumber)) {
            throw BizCode._4601.exception();
        }
        ConsumerUserDO consumerUserDO = new ConsumerUserDO()
                .setUserAccount(getNextUID())
                .setPassword(SaSecureUtil.md5(UUID.randomUUID().toString()))
                .setUsername(phoneNumber)
                .setPhoneNumber(phoneNumber)
                .setRoleCodes("")
                .setCreateWay(CreateWay.PHONE_NUMBER_AUTO)
                .setCreateTime(LocalDateTime.now())
                .setDeleted(false);
        consumerUserMapper.insert(consumerUserDO);
    }

    @Override
    public void manualCreateUser(String phoneNumber, String password) {
        if (consumerUserMapper.isPhoneNumberRegister(phoneNumber)) {
            throw BizCode._4601.exception();
        }
        ConsumerUserDO consumerUserDO = new ConsumerUserDO()
                .setUserAccount(getNextUID())
                .setPassword(SaSecureUtil.md5(password))
                .setUsername(phoneNumber)
                .setPhoneNumber(phoneNumber)
                .setRoleCodes("")
                .setCreateWay(CreateWay.MANUAL_REGISTER)
                .setCreateTime(LocalDateTime.now())
                .setDeleted(false);
        consumerUserMapper.insert(consumerUserDO);
    }

    @Override
    public void retrievePassword(String phoneNumber, String password) {
        ConsumerUserDO consumerUserDO = consumerUserMapper.getByPhoneNumber(phoneNumber);
        if (consumerUserDO == null) {
            throw BizCode._4100.exception();
        }
        consumerUserDO.setPassword(SaSecureUtil.md5(password));
        consumerUserMapper.updateById(consumerUserDO);
    }

    @Override
    public void updateConsumerName(String account, String username) {
        //判断用户是否存在
        ConsumerUserDO consumerUserDO = consumerUserMapper.getByAccount(account);
        if (consumerUserDO == null) {
            throw BizCode._4100.exception();
        }
        consumerUserDO.setUsername(username);
        consumerUserMapper.updateById(consumerUserDO);


    }

    @Override
    public void updatePassword(String phoneNumber, String newPassword) {
        ConsumerUserDO consumerUserDO = consumerUserMapper.getByPhoneNumber(phoneNumber);
        if (consumerUserDO == null) {
            throw BizCode._4603.exception();
        }
        consumerUserDO.setPassword(SaSecureUtil.md5(newPassword));
        consumerUserMapper.updateById(consumerUserDO);
    }

    @Override
    public void bindEmail(String email, String userAccount) {
        //校验该邮箱是否已被绑定
        ConsumerUserDO consumerUserDO = consumerUserMapper.getByEmail(email);
        if (consumerUserDO != null) {
            throw BizCode._4605.exception();
        }
        consumerUserDO = consumerUserMapper.getByAccount(userAccount);
        if (consumerUserDO == null) {
            throw BizCode._4605.exception();
        }
        consumerUserDO.setEmail(email);
        consumerUserMapper.updateById(consumerUserDO);
    }

    private String getNextUID() {
        // TODO UUID不方便记忆，替换为更好的实现
        return IdWorker.get32UUID();
    }
}

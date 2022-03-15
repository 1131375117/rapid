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

    public static final String DEFAULT_PHONE_NUMBER = "未绑定";

    @Override
    public ConsumerLoginUserVO getLoginUserVOWithPassword(String account) {
        if (DEFAULT_PHONE_NUMBER.equals(account)) {
            return null;
        }

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
    public ConsumerUserDO getUserDOById(Long consumerUserId) {
        return consumerUserMapper.selectById(consumerUserId);
    }

    @Transactional
    @Override
    public ConsumerUserDO autoCreateUserByOpenUserId(String username, String openUserId) {
        ConsumerUserDO consumerUserDO = new ConsumerUserDO()
                .setUserAccount(getNextUID())
                .setPassword(SaSecureUtil.md5(UUID.randomUUID().toString()))
                .setUsername(username)
                .setPhoneNumber(DEFAULT_PHONE_NUMBER)
                .setRoleCodes("")
                .setCreateWay(CreateWay.CHANNEL_OPEN_USER_ID_AUTO)
                .setCreateTime(LocalDateTime.now())
                .setDeleted(false);
        consumerUserMapper.insert(consumerUserDO);
        return consumerUserDO;
    }

    private String getNextUID() {
        // TODO UUID不方便记忆，替换为更好的实现
        return IdWorker.get32UUID();
    }
}

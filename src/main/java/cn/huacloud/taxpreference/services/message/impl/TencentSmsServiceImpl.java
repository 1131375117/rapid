package cn.huacloud.taxpreference.services.message.impl;

import cn.huacloud.taxpreference.common.enums.MsgType;
import cn.huacloud.taxpreference.common.enums.SmsBiz;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.message.SmsService;
import cn.huacloud.taxpreference.services.message.entity.dos.MsgRecordDO;
import cn.huacloud.taxpreference.services.message.entity.dtos.TencentSmsParamDTO;
import cn.huacloud.taxpreference.services.message.handler.SmsBizHandler;
import cn.huacloud.taxpreference.services.message.mapper.MsgRecordMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 腾讯云短信服务实现
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TencentSmsServiceImpl implements SmsService {

    private static final String DEFAULT_SENDER = "腾讯云";

    private final SysParamService sysParamService;

    private final MsgRecordMapper msgRecordMapper;

    private final ObjectMapper objectMapper;

    private final List<SmsService.Interceptor> interceptors;

    @Transactional
    @Override
    public void sendSms(List<String> phoneNumbers, SmsBiz smsBiz) {
        // 执行拦截器
        for (Interceptor interceptor : interceptors) {
            interceptor.apply(phoneNumbers, smsBiz);
        }

        // 获取处理器
        SmsBizHandler handler = SpringUtil.getBean(smsBiz.smsBizHandlerClass);

        try {
            // 调用前置处理
            handler.beforeHandle(phoneNumbers);

            // 获取短信参数
            List<String> params = handler.getParams(phoneNumbers);

            // 获取系统参数
            TencentSmsParamDTO smsParams = sysParamService.getObjectParamByTypes(smsBiz.getSysParamTypes(), TencentSmsParamDTO.class);

            // 发送短信
            sendSms(phoneNumbers, params, smsParams);

            // 保存发送记录
            saveMessageRecord(phoneNumbers, params, smsParams, smsBiz);
        } catch (Exception e) {
            log.error("发送短信失败", e);
        } finally {
            // 调用后置处理
            handler.afterHandle(phoneNumbers);
        }
    }

    /**
     * 发送短信
     * @param phoneNumbers 电话号码集合
     * @param params 短信参数
     * @param smsParams 短信系统参数
     */
    private void sendSms(List<String> phoneNumbers, List<String> params, TencentSmsParamDTO smsParams) throws Exception {
        // TODO SmsClient应该实例化，但是修改系统参数后又不能及时生效，先采用实时创建对象的方式实现
        // credential
        Credential credential = new Credential(smsParams.getSecretId(), smsParams.getSecretKey());
        // client profile
        ClientProfile clientProfile = new ClientProfile();
        // sms client
        SmsClient smsClient = new SmsClient(credential, smsParams.getRegion(), clientProfile);
        // request
        SendSmsRequest request = new SendSmsRequest();
        String[] phoneNumberSet = phoneNumbers.stream()
                .distinct()
                .map(phoneNumber -> "+86" + phoneNumber)
                .toArray(String[]::new);
        request.setSmsSdkAppId(smsParams.getSmsSdkAppId());
        request.setSignName(smsParams.getSignName());
        request.setPhoneNumberSet(phoneNumberSet);
        request.setTemplateId(smsParams.getTemplateId());
        // 设置短信参数
        if(!CollectionUtils.isEmpty(params)){
            request.setTemplateParamSet(params.toArray(new String[0]));
        }
        // 执行发送
        smsClient.SendSms(request);
    }

    /**
     * 保存消息记录
     * @param phoneNumbers 电话号码集合
     * @param params 短信参数
     * @param smsParams 短信系统参数
     * @param smsBiz 短信业务
     */
    private void saveMessageRecord(List<String> phoneNumbers, List<String> params, TencentSmsParamDTO smsParams, SmsBiz smsBiz) throws Exception {
        String phoneNumbersStr = phoneNumbers.stream()
                .distinct()
                .collect(Collectors.joining(","));

        String paramsStr = objectMapper.writeValueAsString(params);

        MsgRecordDO msgRecordDO = new MsgRecordDO()
                .setMsgType(MsgType.SMS)
                .setMsgBizType(smsBiz.name())
                .setSender(DEFAULT_SENDER)
                .setReceiver(phoneNumbersStr)
                .setMsgParam(paramsStr)
                .setTemplateId(smsParams.getTemplateId())
                .setCreateTime(LocalDateTime.now());

        msgRecordMapper.insert(msgRecordDO);
    }
}

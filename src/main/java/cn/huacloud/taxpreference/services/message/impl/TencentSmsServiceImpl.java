package cn.huacloud.taxpreference.services.message.impl;

import cn.huacloud.taxpreference.common.enums.SmsBiz;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.message.SmsService;
import cn.huacloud.taxpreference.services.message.entity.dots.TencentSmsParamDTO;
import cn.huacloud.taxpreference.services.message.handler.SmsBizHandler;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 腾讯云短信服务实现
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TencentSmsServiceImpl implements SmsService {

    private final SysParamService sysParamService;

    @Override
    public void sendSms(List<String> phoneNumbers, SmsBiz smsBiz) {
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

            // 短信发送
        } catch (Exception e) {
            log.error("发送短信失败", e);
        } finally {
            // 调用后置处理
            handler.afterHandle(phoneNumbers);
        }
    }

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
        request.setTemplateParamSet(params.toArray(new String[0]));
        // 执行发送
        smsClient.SendSms(request);
    }

}

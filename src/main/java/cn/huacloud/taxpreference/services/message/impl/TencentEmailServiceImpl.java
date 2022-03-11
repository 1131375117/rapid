package cn.huacloud.taxpreference.services.message.impl;

import cn.huacloud.taxpreference.common.enums.EmailBiz;
import cn.huacloud.taxpreference.common.enums.MsgType;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.message.EmailService;
import cn.huacloud.taxpreference.services.message.entity.dos.MsgRecordDO;
import cn.huacloud.taxpreference.services.message.entity.dtos.TencentSesParamDTO;
import cn.huacloud.taxpreference.services.message.handler.EmailBizHandler;
import cn.huacloud.taxpreference.services.message.mapper.MsgRecordMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.ses.v20201002.SesClient;
import com.tencentcloudapi.ses.v20201002.models.SendEmailRequest;
import com.tencentcloudapi.ses.v20201002.models.SendEmailResponse;
import com.tencentcloudapi.ses.v20201002.models.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 邮件服务实现
 *
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TencentEmailServiceImpl implements EmailService {

    private static final String DEFAULT_SENDER = "test@wuxin.biz";

    private final SysParamService sysParamService;

    private final MsgRecordMapper msgRecordMapper;

    private final ObjectMapper objectMapper;

    private final List<EmailService.Interceptor> interceptors;
    private final JavaMailSenderImpl javaMailSender;

    @Override
    @Transactional
    public void sendEmail(List<String> emails, EmailBiz emailBiz) {
        // 执行拦截器
        for (EmailService.Interceptor interceptor : interceptors) {
            interceptor.apply(emails, emailBiz);
        }

        // 获取处理器
        EmailBizHandler emailBizHandler = SpringUtil.getBean(emailBiz.emailBizHandlerClass);

        try {
            // 调用前置处理
            emailBizHandler.beforeHandle(emails);

            // 获取短信参数
            List<String> systemParams = emailBizHandler.getParams(emails);

            // 获取系统参数
            TencentSesParamDTO sesParamDTO = sysParamService.getObjectParamByTypes(emailBiz.getSysParamTypes(), TencentSesParamDTO.class);

            // 发送短信
            sendEmail(emails, systemParams, sesParamDTO);

            // 保存发送记录
            saveMessageRecord(emails, sesParamDTO, emailBiz);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        } finally {
            // 调用后置处理
            emailBizHandler.afterHandle(emails);
        }


    }

    /**
     * @param emails       邮件集合
     * @param systemParams 邮件系统参数
     * @param sesParam     业务参数
     * @throws Exception
     */
    private void sendEmailTencent(List<String> emails, List<String> systemParams, TencentSesParamDTO sesParam) throws Exception {
        // TODO SmsClient应该实例化，但是修改系统参数后又不能及时生效，先采用实时创建对象的方式实现
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        Credential cred = new Credential(sesParam.getSecretId(), sesParam.getSecretKey());
        // 实例化要请求产品的client对象,clientProfile是可选的
        SesClient client = new SesClient(cred, sesParam.getRegion());
        // 实例化一个请求对象,每个接口都会对应一个request对象
        SendEmailRequest req = new SendEmailRequest();
        req.setFromEmailAddress(DEFAULT_SENDER);
        String dest[] = new String[emails.size()];
        for (int i = 0; i < emails.size(); i++) {
            dest[i] = emails.get(i);
        }
        req.setDestination(dest);
        Template template = new Template();
        template.setTemplateID(sesParam.getTemplateId());
        HashMap<String, String> contentMap = new HashMap<>(16);
        contentMap.put("code", systemParams.get(0));
        template.setTemplateData(objectMapper.writeValueAsString(contentMap));
        req.setTemplate(template);
        req.setSubject(sesParam.getSubjectType());
        // 返回的resp是一个SendEmailResponse的实例，与请求对象对应
        SendEmailResponse resp = client.SendEmail(req);
        // 输出json格式的字符串回包
        System.out.println(SendEmailResponse.toJsonString(resp));
    }


    /**
     * @param emails       邮件集合
     * @param systemParams 邮件系统参数
     * @param sesParam     业务参数
     * @throws Exception
     */
    private void sendEmail(List<String> emails, List<String> systemParams, TencentSesParamDTO sesParam) throws Exception {

        //1、创建一个复杂的邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //邮件主题
        helper.setSubject(sesParam.getSubjectType());
        //文本中添加图片
        // helper.addInline("image1",new FileSystemResource("D:\\images\\spring\\1.jpg"));
        //邮件内容
        if (CollectionUtils.isEmpty(systemParams)) {
            helper.setText(sesParam.getTemplate(), true);
        } else {
            helper.setText(String.format(sesParam.getTemplate(), systemParams.get(0)), true);
        }
        helper.setTo(emails.get(0));
        helper.setFrom(sesParam.getSecretId());
        //附件添加图片
        // helper.addAttachment("1.jpg",new File("D:\\images\\spring\\1.jpg"));
        //附件添加word文档
        //  helper.addAttachment("哈哈哈.docx",new File("D:\\images\\spring\\哈哈哈.docx"));

        javaMailSender.send(mimeMessage);
    }

    /**
     * @param emails     邮件号码集合
     * @param sysParamDO 系统参数
     * @param emailBiz   业务类型
     */
    private void saveMessageRecord(List<String> emails, TencentSesParamDTO sysParamDO, EmailBiz emailBiz) throws JsonProcessingException {

        String emailStr = emails.stream()
                .distinct()
                .collect(Collectors.joining(","));

        String paramsStr = objectMapper.writeValueAsString(sysParamDO);

        MsgRecordDO msgRecordDO = new MsgRecordDO()
                .setMsgType(MsgType.EMAIL)
                .setMsgBizType(emailBiz.name())
                .setSender(DEFAULT_SENDER)
                .setReceiver(emailStr)
                .setMsgParam(paramsStr)
                .setTemplateId(String.valueOf(sysParamDO.getTemplateId()))
                .setCreateTime(LocalDateTime.now());

        msgRecordMapper.insert(msgRecordDO);
    }

}

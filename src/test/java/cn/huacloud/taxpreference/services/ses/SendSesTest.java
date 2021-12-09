package cn.huacloud.taxpreference.services.ses;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import com.tencentcloudapi.cvm.v20170312.models.Filter;
import com.tencentcloudapi.ses.v20201002.SesClient;
import com.tencentcloudapi.ses.v20201002.models.SendEmailRequest;
import com.tencentcloudapi.ses.v20201002.models.SendEmailResponse;
import com.tencentcloudapi.ses.v20201002.models.Template;
import org.apache.http.client.CredentialsProvider;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.HashMap;

public class SendSesTest {


	public static void main(String[] args) {

		try {
			// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
			Credential cred = new Credential("AKID0pVzC8nCKBl46sMaXEengIdhU0FDurYy", "A4h37uQfdKktlMhlY4JIzPZT6RVJq64l");

			// 实例化一个http选项，可选的，没有特殊需求可以跳过
			HttpProfile httpProfile = new HttpProfile();
			//  从3.1.16版本开始, 单独设置 HTTP 代理
			// httpProfile.setProxyHost("真实代理ip");
			// httpProfile.setProxyPort(真实代理端口);
			httpProfile.setReqMethod("GET"); // get请求(默认为post请求)
			httpProfile.setConnTimeout(30); // 请求连接超时时间，单位为秒(默认60秒)
			httpProfile.setWriteTimeout(30);  // 设置写入超时时间，单位为秒(默认0秒)
			httpProfile.setReadTimeout(30);  // 设置读取超时时间，单位为秒(默认0秒)
			httpProfile.setEndpoint("ses.tencentcloudapi.com"); // 指定接入地域域名(默认就近接入)

			// 实例化一个client选项，可选的，没有特殊需求可以跳过
			ClientProfile clientProfile = new ClientProfile();
//			clientProfile.setSignMethod("HmacSHA256"); // 指定签名算法(默认为HmacSHA256)
			// 自3.1.80版本开始，SDK 支持打印日志。
			clientProfile.setHttpProfile(httpProfile);
//			clientProfile.setDebug(true);
			// 从3.1.16版本开始，支持设置公共参数 Language, 默认不传，选择(ZH_CN or EN_US)
//			clientProfile.setLanguage(Language.EN_US);
			// 实例化要请求产品(以cvm为例)的client对象,clientProfile是可选的
			SesClient client = new SesClient(cred, "ap-hongkong", clientProfile);
			SendEmailRequest sendEmailRequest = new SendEmailRequest();
			String fromEmailAddress = "test@wuxin.biz";
			sendEmailRequest.setFromEmailAddress(fromEmailAddress);

			String[] replyToAddress = {"wx1755648829@163.com"};
			sendEmailRequest.setDestination(replyToAddress);

			Template template = new Template();
			Long templateID = 21701L;
			template.setTemplateID(templateID);
			sendEmailRequest.setTemplate(template);

			String object = "邮件主题";
			sendEmailRequest.setSubject(object);
			SendEmailResponse sendEmailResponse= client.SendEmail(sendEmailRequest);
			System.out.println(SendEmailResponse.toJsonString(sendEmailResponse));

		} catch (
				Exception e) {
			System.out.println(e.toString());
		}
	}
}


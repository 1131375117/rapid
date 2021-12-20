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
				httpProfile.setEndpoint("ses.tencentcloudapi.com");
				// 实例化一个client选项，可选的，没有特殊需求可以跳过
				ClientProfile clientProfile = new ClientProfile();
				clientProfile.setHttpProfile(httpProfile);
				// 实例化要请求产品的client对象,clientProfile是可选的
				SesClient client = new SesClient(cred, "ap-hongkong", clientProfile);
				// 实例化一个请求对象,每个接口都会对应一个request对象
				SendEmailRequest req = new SendEmailRequest();
				req.setFromEmailAddress("test@wuxin.biz");

				String[] destination1 = {"2132582613@qq.com"};
				req.setDestination(destination1);

				Template template1 = new Template();
				template1.setTemplateID(21717L);
				template1.setTemplateData("{\"code\":\"哈哈哈\"}");
				req.setTemplate(template1);

				req.setSubject("测试");
				// 返回的resp是一个SendEmailResponse的实例，与请求对象对应
				SendEmailResponse resp = client.SendEmail(req);
				// 输出json格式的字符串回包
				System.out.println(SendEmailResponse.toJsonString(resp));
			} catch (TencentCloudSDKException e) {
				System.out.println(e.toString());
			}
		}
}


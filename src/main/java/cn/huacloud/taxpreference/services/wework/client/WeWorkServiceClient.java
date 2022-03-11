package cn.huacloud.taxpreference.services.wework.client;

import cn.huacloud.taxpreference.services.wework.client.config.WeWorkFeignConfiguration;
import cn.huacloud.taxpreference.services.wework.client.entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wangkh
 */
@FeignClient(name = "WeWorkServiceClient", url = "https://qyapi.weixin.qq.com", configuration = WeWorkFeignConfiguration.class)
public interface WeWorkServiceClient {

    /**
     * 获取服务商凭证
     */
    @PostMapping("/cgi-bin/service/get_provider_token")
    ProviderToken getProviderToken(@RequestBody ProviderToken.Request request);

    /**
     * 获取第三方应用凭证
     */
    @PostMapping("/cgi-bin/service/get_suite_token")
    SuiteToken getSuiteToken(@RequestBody SuiteToken.Request request);

    /**
     * 获取企业凭证
     */
    @PostMapping("/cgi-bin/service/get_corp_token?suite_access_token={suite_access_token}")
    CropToken getCropToken(@PathVariable("suite_access_token") String suite_access_token,
                           @RequestBody CropToken.Request request);

    /**
     * 获取企业永久授权码
     */
    @PostMapping("/cgi-bin/service/get_permanent_code?suite_access_token={suite_access_token}")
    PermanentCode getPermanentCode(@PathVariable("suite_access_token") String suite_access_token,
                                   @RequestBody PermanentCode.Request request);

    /**
     * 获取访问用户身份
     */
    @GetMapping("/cgi-bin/service/getuserinfo3rd?suite_access_token={suite_access_token}&code={code}")
    UserInfo3rd getUserInfo3rd(@PathVariable("suite_access_token") String suite_access_token,
                               @PathVariable("code") String code);
}

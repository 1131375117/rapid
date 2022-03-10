package cn.huacloud.taxpreference.services.wework.auth2;

import cn.huacloud.taxpreference.config.WeWorkConfig;
import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wangkh
 */
@Slf4j
@Component
public class WeWorkCheckRedirectComponent {

    public static final String REDIRECT_STATE = "WWX_AUTH_REDIRECT";

    private static final String REDIRECT_URL_PATTERN = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={}&redirect_uri={}&response_type=code&scope={}&state={}#wechat_redirect";

    @Autowired
    private WeWorkConfig weWorkConfig;

    /**
     * 检查请求并重定向
     */
    public void checkAndRedirect(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 检查路径是否满足
            String requestURI = request.getRequestURI();
            AppConfig appConfig = weWorkConfig.getAppConfigByUri(requestURI);
            if (appConfig == null) {
                return;
            }
            // 检查域名是否满足
            List<String> hosts = toList(request.getHeaders("host"));
            if (Collections.disjoint(hosts, appConfig.getHosts())) {
                return;
            }
            // 获取重定向url并重定向
            String redirectUrl = getRedirectUrl(request, appConfig);
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.error("执行重定向失败", e);
        }
    }

    /**
     * 获取重定向url
     */
    private String getRedirectUrl(HttpServletRequest request, AppConfig appConfig) {
        String urlPatten = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={}&redirect_uri={}&response_type=code&scope=snsapi_userinfo&state={}#wechat_redirect";
        String originalUrl = request.getRequestURL().toString();
        List<Object> args = new ArrayList<>();
        args.add(appConfig.getSuiteId());
        args.add(originalUrl);
        args.add(REDIRECT_STATE);
        return MessageFormatter.arrayFormat(urlPatten, args.toArray()).getMessage();
    }

    public static <T> List<T> toList(Enumeration<T> enumeration) {
        List<T> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }
}

package cn.huacloud.taxpreference.services.wework.client.config;

import cn.huacloud.taxpreference.config.WeWorkConfig;
import feign.Capability;
import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * feign client http 代理配置
 * @author wangkh
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public Client proxyClient(WeWorkConfig weWorkConfig) {

        WeWorkConfig.ProxyConfig proxyConfig = weWorkConfig.getProxy();
        if (!proxyConfig.getEnable()) {
            // 不启动http代理
            return new Client.Default(null, null);
        }

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort()));

        if (proxyConfig.useAuth()) {
            return new ProxyClient(null, null, proxy, proxyConfig.getUsername(), proxyConfig.getPassword());
        } else {

            return new ProxyClient(null, null, proxy);
        }
    }

    /**
     * http代理客户端，只有实现Capability接口才能被feign自动配置
     */
    public static class ProxyClient extends Client.Proxied implements Capability {

        public ProxyClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, Proxy proxy) {
            super(sslContextFactory, hostnameVerifier, proxy);
        }

        public ProxyClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, Proxy proxy, String proxyUser, String proxyPassword) {
            super(sslContextFactory, hostnameVerifier, proxy, proxyUser, proxyPassword);
        }
    }
}

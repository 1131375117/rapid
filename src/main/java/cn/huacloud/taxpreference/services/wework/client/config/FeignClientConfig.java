package cn.huacloud.taxpreference.services.wework.client.config;

import feign.Capability;
import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * feign client http 代理配置
 * @author wangkh
 */
@Configuration
public class FeignClientConfig {
    public static String proxyHost = "1.117.164.184";
    public static Integer proxyPort = 32000;

    @Bean
    public Client proxyClient(Environment environment) {
        Set<String> profiles = Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toSet());
        if (!profiles.contains("dev")) {
            return new Client.Default(null, null);
        }
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        ProxyClient proxyClient = new ProxyClient(null, null, proxy);
        return proxyClient;
    }

    static class ProxyClient extends Client.Proxied implements Capability {

        public ProxyClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, Proxy proxy) {
            super(sslContextFactory, hostnameVerifier, proxy);
        }

        public ProxyClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, Proxy proxy, String proxyUser, String proxyPassword) {
            super(sslContextFactory, hostnameVerifier, proxy, proxyUser, proxyPassword);
        }
    }
}

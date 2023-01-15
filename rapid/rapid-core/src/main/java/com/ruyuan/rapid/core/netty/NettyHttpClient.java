package com.ruyuan.rapid.core.netty;

import java.io.IOException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import com.ruyuan.rapid.core.LifeCycle;
import com.ruyuan.rapid.core.RapidConfig;
import com.ruyuan.rapid.core.helper.AsyncHttpHelper;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * <B>主类名称：</B>NettyHttpClient<BR>
 * <B>概要说明：</B>HTTP客户端启动类，主要用于下游服务的请求转发<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午10:20:30
 */
@Slf4j
public class NettyHttpClient implements LifeCycle {

	private AsyncHttpClient asyncHttpClient;
	
	private DefaultAsyncHttpClientConfig.Builder clientBuilder;
	
	private RapidConfig rapidConfig;
	
	private EventLoopGroup eventLoopGroupWork;
	
	public NettyHttpClient(RapidConfig rapidConfig, EventLoopGroup eventLoopGroupWork) {
		this.rapidConfig = rapidConfig;
		this.eventLoopGroupWork = eventLoopGroupWork;
		//	在构造函数调用初始化方法
		init();
	}
	
	/**
	 * <B>方法名称：</B>init<BR>
	 * <B>概要说明：</B>初始化AsyncHttpClient<BR>
	 * @author  JiFeng
	 * @since 2021年12月5日 下午10:22:10
	 * @see com.ruyuan.rapid.core.LifeCycle#init()
	 */
	@Override
	public void init() {
		this.clientBuilder = new DefaultAsyncHttpClientConfig.Builder()
				.setFollowRedirect(false)
				.setEventLoopGroup(eventLoopGroupWork)
				.setConnectTimeout(rapidConfig.getHttpConnectTimeout())
				.setRequestTimeout(rapidConfig.getHttpRequestTimeout())
				.setMaxRequestRetry(rapidConfig.getHttpMaxRequestRetry())
				.setAllocator(PooledByteBufAllocator.DEFAULT)
				.setCompressionEnforced(true)
				.setMaxConnections(rapidConfig.getHttpMaxConnections())
				.setMaxConnectionsPerHost(rapidConfig.getHttpConnectionsPerHost())
				.setPooledConnectionIdleTimeout(rapidConfig.getHttpPooledConnectionIdleTimeout());
	}

	@Override
	public void start() {
		this.asyncHttpClient = new DefaultAsyncHttpClient(clientBuilder.build());
		AsyncHttpHelper.getInstance().initialized(asyncHttpClient);
	}

	@Override
	public void shutdown() {
		if(asyncHttpClient != null) {
			try {
				this.asyncHttpClient.close();
			} catch (IOException e) {
				// ignore
				log.error("#NettyHttpClient.shutdown# shutdown error", e);
			}
		}
	}

}

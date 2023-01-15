package com.ruyuan.rapid.core;

import com.ruyuan.rapid.common.constants.RapidBufferHelper;
import com.ruyuan.rapid.core.netty.NettyHttpClient;
import com.ruyuan.rapid.core.netty.NettyHttpServer;
import com.ruyuan.rapid.core.netty.processor.NettyBatchEventProcessor;
import com.ruyuan.rapid.core.netty.processor.NettyCoreProcessor;
import com.ruyuan.rapid.core.netty.processor.NettyMpmcProcessor;
import com.ruyuan.rapid.core.netty.processor.NettyProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * <B>主类名称：</B>RapidContainer<BR>
 * <B>概要说明：</B>主流程的容器类<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午10:01:47
 */
@Slf4j
public class RapidContainer implements LifeCycle {

	private final RapidConfig rapidConfig;		//	核心配置类
	
	private NettyHttpServer nettyHttpServer;	//	接收http请求的server
	
	private NettyHttpClient nettyHttpClient;	//	http转发的核心类
	
	private NettyProcessor nettyProcessor;		//	核心处理器
	
	public RapidContainer(RapidConfig rapidConfig) {
		this.rapidConfig = rapidConfig;
		init();
	}
	
	@Override
	public void init() {
		//	1. 构建核心处理器
		NettyCoreProcessor nettyCoreProcessor = new NettyCoreProcessor();
		
		//	2. 是否开启缓存
		String bufferType = rapidConfig.getBufferType();
		
		if(RapidBufferHelper.isFlusher(bufferType)) {
			nettyProcessor = new NettyBatchEventProcessor(rapidConfig, nettyCoreProcessor);
		}
		else if(RapidBufferHelper.isMpmc(bufferType)) {
			nettyProcessor = new NettyMpmcProcessor(rapidConfig, nettyCoreProcessor);
		}
		else {
			nettyProcessor = nettyCoreProcessor;
		}
		//	3. 创建NettyhttpServer
		nettyHttpServer = new NettyHttpServer(rapidConfig, nettyProcessor);
		
		//	4. 创建NettyHttpClient
		nettyHttpClient = new NettyHttpClient(rapidConfig, nettyHttpServer.getEventLoopGroupWork());
		
	}

	@Override
	public void start() {
		nettyProcessor.start();
		nettyHttpServer.start();
		nettyHttpClient.start();
		log.info("RapidContainer started !");
	}

	@Override
	public void shutdown() {
		nettyProcessor.shutdown();
		nettyHttpServer.shutdown();
		nettyHttpClient.shutdown();
	}

}

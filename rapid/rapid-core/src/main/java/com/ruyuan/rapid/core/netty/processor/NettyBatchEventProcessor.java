package com.ruyuan.rapid.core.netty.processor;

import com.ruyuan.rapid.core.RapidConfig;
import com.ruyuan.rapid.core.context.HttpRequestWrapper;

/**
 * <B>主类名称：</B>NettyBatchEventProcessor<BR>
 * <B>概要说明：</B>flusher缓冲队列的核心实现, 最终调用的方法还是要回归到NettyCoreProcessor<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午10:11:16
 */
public class NettyBatchEventProcessor implements NettyProcessor {

	private RapidConfig rapidConfig;
	
	private NettyCoreProcessor nettyCoreProcessor;
	
	public NettyBatchEventProcessor(RapidConfig rapidConfig, NettyCoreProcessor nettyCoreProcessor) {
		this.rapidConfig = rapidConfig;
		this.nettyCoreProcessor = nettyCoreProcessor;
	}

	@Override
	public void process(HttpRequestWrapper httpRequestWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
	
}

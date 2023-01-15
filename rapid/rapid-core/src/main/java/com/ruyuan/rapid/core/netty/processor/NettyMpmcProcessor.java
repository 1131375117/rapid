package com.ruyuan.rapid.core.netty.processor;

import com.ruyuan.rapid.core.RapidConfig;
import com.ruyuan.rapid.core.context.HttpRequestWrapper;

/**
 * <B>主类名称：</B>NettyMpmcProcessor<BR>
 * <B>概要说明：</B>mpmc的核心实现处理器, 最终我们还是要使用NettyCoreProcessor<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午10:13:33
 */
public class NettyMpmcProcessor implements NettyProcessor {
	
	private RapidConfig rapidConfig;
	
	private NettyCoreProcessor nettyCoreProcessor;

	public NettyMpmcProcessor(RapidConfig rapidConfig, NettyCoreProcessor nettyCoreProcessor) {
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

package com.ruyuan.rapid.core.netty.processor;

import com.ruyuan.rapid.core.context.HttpRequestWrapper;

/**
 * <B>主类名称：</B>NettyProcessor<BR>
 * <B>概要说明：</B>处理Netty核心逻辑的执行器接口定义<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午9:48:26
 */
public interface NettyProcessor {

	/**
	 * <B>方法名称：</B>process<BR>
	 * <B>概要说明：</B>核心执行方法<BR>
	 * @author JiFeng
	 * @since 2021年12月5日 下午9:49:36
	 * @param httpRequestWrapper
	 */
	public void process(HttpRequestWrapper httpRequestWrapper);
	
	/**
	 * <B>方法名称：</B>start<BR>
	 * <B>概要说明：</B>执行器启动方法<BR>
	 * @author JiFeng
	 * @since 2021年12月5日 下午9:50:13
	 */
	public void start();
	
	/**
	 * <B>方法名称：</B>shutdown<BR>
	 * <B>概要说明：</B>执行器资源释放/关闭方法<BR>
	 * @author JiFeng
	 * @since 2021年12月5日 下午9:50:15
	 */
	public void shutdown();
	
}

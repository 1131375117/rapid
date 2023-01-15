package com.ruyuan.rapid.core;

/**
 * <B>主类名称：</B>LifeCycle<BR>
 * <B>概要说明：</B>生命周期管理接口<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午6:30:29
 */
public interface LifeCycle {

	/**
	 * <B>方法名称：</B>init<BR>
	 * <B>概要说明：</B>生命周期组件的初始化动作<BR>
	 * @author JiFeng
	 * @since 2021年12月5日 下午6:30:51
	 */
	void init();
	
	/**
	 * <B>方法名称：</B>start<BR>
	 * <B>概要说明：</B>生命周期组件的启动方法<BR>
	 * @author JiFeng
	 * @since 2021年12月5日 下午6:31:08
	 */
	void start();
	
	/**
	 * <B>方法名称：</B>shutdown<BR>
	 * <B>概要说明：</B>生命周期组件的关闭方法<BR>
	 * @author JiFeng
	 * @since 2021年12月5日 下午6:31:28
	 */
	void shutdown();
	
	
}

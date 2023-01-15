package com.ruyuan.rapid.core.context;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

/**
 * <B>主类名称：</B>HttpRequestWrapper<BR>
 * <B>概要说明：</B>请求包装类<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午9:44:12
 */
@Data
public class HttpRequestWrapper {

	private FullHttpRequest fullHttpRequest;
	
	private ChannelHandlerContext ctx;
	
}

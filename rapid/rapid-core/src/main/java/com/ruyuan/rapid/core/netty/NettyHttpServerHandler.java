package com.ruyuan.rapid.core.netty;

import com.ruyuan.rapid.core.context.HttpRequestWrapper;
import com.ruyuan.rapid.core.netty.processor.NettyCoreProcessor;
import com.ruyuan.rapid.core.netty.processor.NettyProcessor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <B>主类名称：</B>NettyHttpServerHandler<BR>
 * <B>概要说明：</B>Netty核心处理handler<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午7:25:26
 */
@Slf4j
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter { // SimpleChannelInboundHandler
	
	private NettyProcessor nettyProcessor;
	
	public NettyHttpServerHandler(NettyProcessor nettyProcessor) {
		this.nettyProcessor = nettyProcessor;
	}
	
	/**
	 * <B>方法名称：</B>channelRead<BR>
	 * <B>概要说明：</B>核心的请求处理方法<BR>
	 * @author  JiFeng
	 * @since 2021年12月5日 下午9:33:09
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		if(msg instanceof HttpRequest) {
			FullHttpRequest request = (FullHttpRequest)msg;
			HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper();
			httpRequestWrapper.setFullHttpRequest(request);
			httpRequestWrapper.setCtx(ctx);
			
			//	processor
			nettyProcessor.process(httpRequestWrapper);
			
		} else {
			//	never go this way, ignore
			log.error("#NettyHttpServerHandler.channelRead# message type is not httpRequest: {}", msg);
			boolean release = ReferenceCountUtil.release(msg);
			if(!release) {
				log.error("#NettyHttpServerHandler.channelRead# release fail 资源释放失败");
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	

}

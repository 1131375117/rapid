package com.ruyuan.rapid.common.constants;

/**
 * <B>主类名称：</B>RapidBufferHelper<BR>
 * <B>概要说明：</B>网关缓冲区辅助类<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午5:39:46
 */
public interface RapidBufferHelper {

	String FLUSHER = "FLUSHER";
	
	String MPMC = "MPMC";
	
	static boolean isMpmc(String bufferType) {
		return MPMC.equals(bufferType);
	}
	
	static boolean isFlusher(String bufferType) {
		return FLUSHER.equals(bufferType);
	}
	
}

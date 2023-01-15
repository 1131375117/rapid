
package com.ruyuan.rapid.common.exception;

import com.ruyuan.rapid.common.enums.ResponseCode;

/**
 * <B>主类名称：</B>RapidResponseException<BR>
 * <B>概要说明：</B>所有的响应异常基础定义<BR>
 * @author JiFeng
 * @since 2021年12月5日 下午4:41:12
 */
public class RapidResponseException extends RapidBaseException {

    private static final long serialVersionUID = -5658789202509039759L;

    public RapidResponseException() {
        this(ResponseCode.INTERNAL_ERROR);
    }

    public RapidResponseException(ResponseCode code) {
        super(code.getMessage(), code);
    }

    public RapidResponseException(Throwable cause, ResponseCode code) {
        super(code.getMessage(), cause, code);
        this.code = code;
    }

}

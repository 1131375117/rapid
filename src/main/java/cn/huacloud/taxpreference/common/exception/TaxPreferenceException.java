package cn.huacloud.taxpreference.common.exception;

/**
 * 税收优惠公共父类异常，便于做全局异常管理
 * @author wangkh
 */
public class TaxPreferenceException extends RuntimeException {

    private int code;

    public TaxPreferenceException() {
    }

    public TaxPreferenceException(String message) {
        super(message);
    }

    public TaxPreferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaxPreferenceException(Throwable cause) {
        super(cause);
    }

    public TaxPreferenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

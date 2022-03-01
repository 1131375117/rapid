package cn.huacloud.taxpreference.common.enums;

/**
 * 限流策略
 * 
 * @author tanghc
 */
public enum LimitType {
    /**
     * 窗口策略。每秒处理固定数量的请求，超出请求返回错误信息。
     */
    LEAKY_BUCKET(1),
    /**
     * 令牌桶策略，每秒放置固定数量的令牌数，每个请求进来后先去拿令牌，拿到了令牌才能继续，拿不到则等候令牌重新生成了再拿。
     */
    TOKEN_BUCKET(2);

    private byte type;

    LimitType(int type) {
        this.type = (byte)type;
    }

    public byte getType() {
        return type;
    }

}

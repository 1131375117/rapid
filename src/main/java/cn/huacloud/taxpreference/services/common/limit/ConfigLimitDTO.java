package cn.huacloud.taxpreference.services.common.limit;

import lombok.Data;

/**
 * @author fuhua
 */
@Data
public class ConfigLimitDTO {

    public static final byte LIMIT_STATUS_OPEN = 1;
    public static final byte LIMIT_STATUS_CLOSE = 0;

    /**
     * id
     */
    private Long id;
    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求路径
     */
    private String path;
    /**
     * 请求akId
     */
    private String akId;

    private String ip;


    /**
     * 限流策略，1：窗口策略，2：令牌桶策略, 数据库字段：limit_type
     */
    private byte limitType;

    /**
     * 每秒可处理请求数, 数据库字段：exec_count_per_second
     */
    private Integer execCountPerSecond;

    /**
     * 限流过期时间，默认1秒，即每durationSeconds秒允许多少请求（当limit_type=1时有效）, 数据库字段：durationSeconds
     */
    private Integer durationSeconds;


    /**
     * 限流开启状态，1:开启，0关闭, 数据库字段：limit_status
     */
    private Byte limitStatus;
    /**
     * 每秒生产令牌数量
     */
    private Integer replenishRate;
    /**
     * 桶容量（并发数量）
     */
    private Integer burstCapacity;
    /**
     * 桶每次请求消耗令牌数量
     */
    private Integer requestedTokens;

}

package cn.huacloud.taxpreference.sync.spider;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 作业参数
 * @author wangkh
 */
@Data
public class DataSyncJobParam {
    /**
     * 最大时间
     */
    public static final LocalDateTime MAX_LOCAL_DATE_TIME = LocalDateTime.parse("9999-12-31T23:59:59.999", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    /**
     * 开始时间
     */
    private LocalDateTime from;
    /**
     * 结束时间
     */
    private LocalDateTime to = MAX_LOCAL_DATE_TIME;
}

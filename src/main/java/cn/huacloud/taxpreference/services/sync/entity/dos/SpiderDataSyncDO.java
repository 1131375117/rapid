package cn.huacloud.taxpreference.services.sync.entity.dos;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.sync.SyncStatus;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 爬虫数据同步实体
 * @author wangkh
 */
@Accessors(chain = true)
@Data
@TableName("t_spider_data_sync")
public class SpiderDataSyncDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 文档类型
     */
    private DocType docType;
    /**
     * 爬虫数据ID
     */
    private String spiderDataId;
    /**
     * 文档ID
     */
    private Long docId;
    /**
     * 同步状态
     */
    private SyncStatus syncStatus;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 同步历史记录（保留最近10次）
     */
    private String syncHistory;
}

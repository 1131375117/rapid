package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据同步结果
 * @author wangkh
 */
@Getter
@Setter
public class DataSyncResult extends SpiderUrlHolder {
    /**
     * 文档ID
     */
    private Long docId;

    public static DataSyncResult of(Long docId, String spiderUrl) {
        DataSyncResult dataSyncResult = new DataSyncResult();
        dataSyncResult.setDocId(docId);
        dataSyncResult.setSpiderUrl(spiderUrl);
        return dataSyncResult;
    }
}

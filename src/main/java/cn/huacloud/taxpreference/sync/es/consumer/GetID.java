package cn.huacloud.taxpreference.sync.es.consumer;

/**
 * @author wangkh
 */
public interface GetID<T> {

    /**
     * 获取主键ID
     * @return id
     */
    T getId();
}

package cn.huacloud.taxpreference.services.consumer.entity;

import cn.huacloud.taxpreference.sync.es.consumer.IDGetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 无装饰的组合文本
 * @author wangkh
 */
@Getter
@Setter
public abstract class AbstractCombinePlainContent<T> implements IDGetter<T> {

    /**
     * 无装饰的组合文本
     */
    protected String combinePlainContent;

    /**
     * 获取需要组合的文本list
     * @return 文本list
     */
    public abstract List<CombineText> combineTextList();

    /**
     * 初始化其他组合字段
     */
    public void initialOtherCombineFields() {
    }
}

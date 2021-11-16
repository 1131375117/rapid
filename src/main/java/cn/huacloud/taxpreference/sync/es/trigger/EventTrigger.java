package cn.huacloud.taxpreference.sync.es.trigger;

import cn.huacloud.taxpreference.common.enums.SysCodeGetter;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import reactor.core.publisher.Sinks;

/**
 * 事件触发器
 *
 * @author wangkh
 */
public abstract class EventTrigger<T, R> {

    private static final int PAGE_SIZE = 1000;

    protected Sinks.Many<R> saveMany = Sinks.many().unicast().onBackpressureBuffer();

    protected Sinks.Many<T> deleteMany = Sinks.many().unicast().onBackpressureBuffer();

    public void saveEvent(T id) {
        R entity = getEntityById(id);
        if (entity != null) {
            saveMany.tryEmitNext(entity);
        }
    }

    public void deleteEvent(T id) {
        deleteMany.tryEmitNext(id);
    }

    /**
     * 通过Id获取ES数据实体
     * @param id 主键ID
     * @return ES数据实体
     */
    protected abstract R getEntityById(T id);

    /**
     * 有效数据主键分页查询查询
     * @param queryPage 分页查询条件
     * @return 数据主键
     */
    protected abstract IPage<T> pageIdList(IPage<?> queryPage);

    /**
     * 同步所有数据
     */
    public void syncAll() {
        IPage<?> queryPage = new Page<>(1, PAGE_SIZE);

        // 查询第一页

        IPage<T> firstPage = pageIdList(queryPage);

        long total = firstPage.getTotal();

        for (T id : firstPage.getRecords()) {
            saveEvent(id);
        }

        long totalPage = total / PAGE_SIZE + 1;

        // 分页执行后续数据保存
        for (int i = 2; i <= totalPage; i++) {
            queryPage.setCurrent(i);
            IPage<T> page = pageIdList(queryPage);
            for (T id : page.getRecords()) {
                saveEvent(id);
            }
        }
    }


    public SysCodeSimpleVO getEnumSysCode(SysCodeGetter sysCodeGetter) {
        if (sysCodeGetter == null) {
            return new SysCodeSimpleVO();
        }
        return sysCodeGetter.getSysCode();
    }
}

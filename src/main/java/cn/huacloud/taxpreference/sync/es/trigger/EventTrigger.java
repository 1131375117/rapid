package cn.huacloud.taxpreference.sync.es.trigger;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.SysCodeGetter;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 事件触发器
 *
 * @author wangkh
 */
public abstract class EventTrigger<T, R> {

    private static final int PAGE_SIZE = 1000;

    protected Sinks.Many<R> saveMany = Sinks.many().unicast().onBackpressureBuffer();

    protected Sinks.Many<T> deleteMany = Sinks.many().unicast().onBackpressureBuffer();

    public abstract DocType docType();

    /**
     * 触发保存事件
     * 这里的保存概念包括新增和修改
     * @param id 主键ID
     */
    public void saveEvent(T id) {
        R entity = getEntityById(id);
        if (entity != null) {
            saveMany.tryEmitNext(entity);
        }
    }

    /**
     * 触发删除事件
     * @param id 主键ID
     */
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
     * @return 数据主键
     */
    protected abstract IPage<T> pageIdList(int pageNum, int pageSize);

    /**
     * 同步所有数据
     */
    public long syncAll() {

        // 查询第一页

        IPage<T> firstPage = pageIdList(1, PAGE_SIZE);

        long total = firstPage.getTotal();

        for (T id : firstPage.getRecords()) {
            saveEvent(id);
        }

        long totalPage = total / PAGE_SIZE + 1;

        // 分页执行后续数据保存
        for (int i = 2; i <= totalPage; i++) {
            IPage<T> page = pageIdList(i, PAGE_SIZE);
            for (T id : page.getRecords()) {
                saveEvent(id);
            }
        }
        return total;
    }

    public SysCodeSimpleVO getEnumSysCode(SysCodeGetter sysCodeGetter) {
        if (sysCodeGetter == null) {
            return new SysCodeSimpleVO();
        }
        return sysCodeGetter.getSysCode();
    }

    public <E> Page<T> mapToIdPage(IPage<E> page, Function<E, T> mapper) {
        List<T> records = page.getRecords().stream()
                .map(mapper)
                .collect(Collectors.toList());
        Page<T> idPage = Page.of(page.getCurrent(), page.getSize(), page.getTotal());
        idPage.setRecords(records);
        return idPage;
    }

    public List<String> split2List(String value) {
        if (StringUtils.isBlank(value)) {
            return new ArrayList<>();
        }
        return Arrays.asList(value.split(","));
    }
}

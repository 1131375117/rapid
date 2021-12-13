package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;

import java.util.List;
import java.util.Map;

/**
 * @author fuhua
 */
public interface SysParamService {
    /**
     * 根据paramKey查询
     *
     * @param paramKey
     * @return
     */
    SysParamDO selectByParamKey(String paramKey);

    /**
     * 根据指定参数类型返回数据对象
     *
     * @param sysParamTypes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getObjectParamByTypes(List<String> sysParamTypes, Class<T> clazz);

    /**
     * 根据传入类型返回指定类型
     *
     * @param clazz
     * @param args
     * @param <T>
     * @return
     */
    <T> Map<String, T> getMapParamByTypes(Class<T> clazz, String... args);
}

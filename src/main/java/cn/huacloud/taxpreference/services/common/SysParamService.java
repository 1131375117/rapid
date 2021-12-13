package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import com.fasterxml.jackson.core.JsonProcessingException;

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
     * @param sysParamTypes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getObjectParamByTypes(List<String> sysParamTypes, Class<T> clazz) throws InstantiationException, IllegalAccessException;

    <T> Map<String, T> getMapParamByTypes(Class<T> clazz, String... args) throws JsonProcessingException;

    /**
     * 获取单一参数值
     *
     * @param sysParamType 参数类型
     * @param sysParamKey  参数Key
     * @param clazz        参数类型
     * @param defaultValue 默认值
     * @return 参数单一值
     */
    default <T> T getSingleParamValue(String sysParamType, String sysParamKey, Class<T> clazz, T defaultValue) {
        T value = getSingleParamValue(sysParamType, sysParamKey, clazz);
        return value == null ? defaultValue : value;
    }

    /**
     * 获取单一参数值
     *
     * @param sysParamType 参数类型
     * @param sysParamKey  参数Key
     * @param clazz        参数类型
     * @return 参数单一值
     */
    <T> T getSingleParamValue(String sysParamType, String sysParamKey, Class<T> clazz);
}

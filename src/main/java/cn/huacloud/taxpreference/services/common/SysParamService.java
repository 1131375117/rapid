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
     *
     * @param sysParamTypes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getObjectParamByTypes(List<String> sysParamTypes, Class<T> clazz) throws InstantiationException, IllegalAccessException;

    <T> Map<String, T> getMapParamByTypes(Class<T> clazz, String... args) throws JsonProcessingException;
}

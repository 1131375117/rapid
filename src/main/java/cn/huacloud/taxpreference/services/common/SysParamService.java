package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.handler.param.SysParamHandler;

import java.util.List;
import java.util.Map;

/**
 * 系统参数服务
 * @author fuhua
 */
public interface SysParamService extends CacheClear {

    /**
     * 更加系统码值
     * @param paramType 系统参数类型
     * @param paramKey 参数Key
     * @return 系统参数
     */
    SysParamDO getSysParamDO(String paramType, String paramKey);

    /**
     * 根据系统参数类型获取参数对象， paramKey -> 属性名称，paramValue -> 属性值
     * 需要参数key符合java属性命名规则
     *
     * @param sysParamTypes 系统参数类型集合
     * @param clazz 对象的类型，会自动创建对象（需要无参构造器）
     * @return 参数对象
     */
    <T> T getObjectParamByTypes(List<String> sysParamTypes, Class<T> clazz);

    /**
     * 根据系统参数类型获取参数map，key -> paramKey，value -> paramValue
     *
     * @param clazz map的value类型，会自动转换，无法完成转换会返回空map
     * @param sysParamTypes 系统参数类型
     * @return 参数map
     */
    <T> Map<String, T> getMapParamByTypes(Class<T> clazz, String... sysParamTypes);

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
    <T> T getSingleParamValue(String sysParamType, String sysParamKey, Class<T> clazz) ;

    /**
     * 通过参数处理器来获取参数
     * @param handlerClass 参数处理器类型
     * @return 处理好的参数
     */
    <T, R> R getParamByHandler(Class<? extends SysParamHandler<T, R>> handlerClass, T handlerParam, List<String> paramTypes);

    /**
     * 通过系统参数类型从缓存中获取系统参数
     *
     * @param paramTypes 系统参数类型
     * @return 系统参数集合
     */
    List<SysParamDO> getSysParamDOByTypes(String... paramTypes);
}

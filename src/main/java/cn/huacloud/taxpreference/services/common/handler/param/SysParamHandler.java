package cn.huacloud.taxpreference.services.common.handler.param;

import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;

import java.util.List;

/**
 * @author wangkh
 */
public interface SysParamHandler<T> {

    /**
     * 获取参数类型
     */
    List<String> getParamTypes();

    /**
     * 处理系统参数
     * @param sysParams 系统参数集合
     * @return 处理封装好的系统参数
     */
    T handle(List<SysParamDO> sysParams);
}

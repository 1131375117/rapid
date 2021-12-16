package cn.huacloud.taxpreference.services.common.handler.param;

import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;

import java.util.List;

/**
 * @author wangkh
 */
public interface SysParamHandler<T, R> {

    /**
     * 处理系统参数
     * @param sysParams 系统参数集合
     * @param handlerParam 处理器参数
     * @return 处理封装好的系统参数
     */
    R handle(List<SysParamDO> sysParams, T handlerParam);
}

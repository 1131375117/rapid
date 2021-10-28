package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;

import java.util.List;

/**
 * @author wangkh
 */
public interface SysCodeService {

    /**
     * 根据系统码值类型获取系统码值
     * @param sysCodeType 系统码值类型
     * @return 系统码值
     */
    List<SysCodeTreeVO> getSysCodeTreeVO(SysCodeType sysCodeType);
}

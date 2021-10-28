package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
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

    /**
     * 根据码值代码获取码值名称
     * 已做缓存处理，无需担心效率
     * @param codeValue 码值代码
     * @return 码值名称 如果码值不存在则返回null
     */
    String getCodeNameByCodeValue(String codeValue);
}

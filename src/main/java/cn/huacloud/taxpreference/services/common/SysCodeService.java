package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;

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
     * 根据码值代码获取码值名称（包括失效的系统码值，兼顾旧数据）
     * 已做缓存处理，无需担心效率
     * @param codeValue 码值代码
     * @return 码值名称 如果码值不存在则返回null
     */
    String getCodeNameByCodeValue(String codeValue);

    /**
     * 码字字符串集合获取视图对象集合
     * @param codeValues 多个系统码值，以","分割
     * @return
     */
    List<SysCodeVO> getListByCodeValues(String codeValues);
}

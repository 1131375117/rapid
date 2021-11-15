package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;

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
     * @return 码值视图集合
     */
    List<SysCodeVO> getListByCodeValues(String codeValues);

    /**
     * 码值拼接字符串获取名字拼接字符串
     * @param codeValues 多个系统码值，以","分割
     * @return 名字拼接字符串
     */
    String getStringNamesByCodeValues(String codeValues);

    /**
     * 根据类型和名称获取码值对象视图
     * 不包含失效码值
     * @param codeType 码值类型
     * @param codeName 码值名称
     * @return 码值视图，没有匹配码值返回 null
     */
    SysCodeVO getCodeVOByCodeName(SysCodeType codeType, String codeName);

    /**
     * 码字字符串集合获取简单视图对象集合
     * @param codeValues 多个系统码值，以","分割
     * @return 码值搜简单图集合
     */
    List<SysCodeSimpleVO> getSimpleVOListByCodeValues(String codeValues);

    /**
     * 根据系统码值获取码值简单视图
     * @param codeValue 系统码值
     * @return 码值视图
     */
    SysCodeSimpleVO getSimpleVOByCode(String codeValue);
}

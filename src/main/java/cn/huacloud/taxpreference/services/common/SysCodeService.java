package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.SysCodeQueryDTO;
import cn.huacloud.taxpreference.services.common.entity.dtos.SysCodeStringDTO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;

import java.util.Collection;
import java.util.List;

/**
 * @author wangkh
 */
public interface SysCodeService extends CacheClear {

    /**
     * 根据系统码值类型获取系统码值
     * @param sysCodeType 系统码值类型
     * @return 系统码值
     */
    List<SysCodeTreeVO> getSysCodeTreeVO(SysCodeType sysCodeType);

    /**
     * 根据码值代码获取码值名称（包括失效的系统码值，兼顾旧数据）
     * 已做缓存处理，无需担心效率
     * @deprecated 码值不再全局唯一，需要传入码值类型和codeValue, getSysCodeDO
     * @param codeValue 码值代码
     * @return 码值名称 如果码值不存在则返回null
     */
    @Deprecated
    String getCodeNameByCodeValue(String codeValue);

    /**
     * 获取码值名称，找不到返回空串
     * @param sysCodeType 系统码值类型
     * @param codeValue 系统码值
     * @return 码值名称
     */
    String getSysCodeName(SysCodeType sysCodeType, String codeValue);

    /**
     * 获取系统码值对象
     * @param sysCodeType 系统码值类型
     * @param codeValue 系统码值
     * @return 系统码值对象
     */
    SysCodeDO getSysCodeDO(SysCodeType sysCodeType, String codeValue);


    /**
     * 根据系统码值集合，获取 codes 和 names
     * 默认取传入码值的第一个类型，其他类型将会忽略
     * @param codes 系统码值集合
     * @param withChildren 是否加入子节点
     * @return 系统码值字符串传输对象
     */
    @Deprecated
    SysCodeStringDTO getSysCodeStringDTO(Collection<String> codes, boolean withChildren);

    /**
     * 根据系统码值集合，获取 codes 和 names
     * 默认取传入码值的第一个类型，其他类型将会忽略
     * @param codes 系统码值集合
     * @param withChildren 是否加入子节点
     * @return 系统码值字符串传输对象
     */
    SysCodeStringDTO getSysCodeStringDTO(SysCodeType sysCodeType, Collection<String> codes, boolean withChildren);

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
    @Deprecated
    List<SysCodeSimpleVO> getSimpleVOListByCodeValues(String codeValues);

    /**
     * 码字字符串集合获取简单视图对象集合
     * @param codeValues 多个系统码值，以","分割
     * @return 码值搜简单图集合
     */
    List<SysCodeSimpleVO> getSimpleVOListByCodeValues(SysCodeType sysCodeType, String codeValues);

    /**
     * 根据系统码值获取码值简单视图
     * @param codeValue 系统码值
     * @return 码值视图
     */
    @Deprecated
    SysCodeSimpleVO getSimpleVOByCode(String codeValue);

    /**
     * 根据系统码值获取码值简单视图
     * @param codeValue 系统码值
     * @return 码值视图
     */
    @Deprecated
    SysCodeSimpleVO getSimpleVOByCode(SysCodeType sysCodeType, String codeValue);

    /**
     * 通过码值类型获取系统码值
     * @param sysCodeType 系统码值类型
     * @return 系统码值DO list
     */
    List<SysCodeDO> getSysCodeDOByCodeType(SysCodeType sysCodeType);

    /**
     * 获取码值本身和其叶子节点
     * @param target 目标码值
     * @return 码值集合包含叶子节点
     */
    @Deprecated
    List<String> withChildrenCodes(Collection<?> target);

    /**
     * 获取码值本身和其叶子节点
     * @param target 目标码值
     * @return 码值集合包含叶子节点
     */
    List<String> withChildrenCodes(SysCodeType sysCodeType, Collection<?> target);

    /**
     * 懒加载获取系统码值
     * @param sysCodeQueryDTO 系统码值查询对象
     * @return
     */
    List<SysCodeTreeVO> getSysCodeTreeVOLazy(SysCodeQueryDTO sysCodeQueryDTO);
}

package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.entity.vos.TreeVO;
import cn.huacloud.taxpreference.common.enums.SysCodeStatus;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;
import cn.huacloud.taxpreference.services.common.mapper.SysCodeMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统码值服务实现
 *
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysCodeServiceImpl implements SysCodeService {

    private final SysCodeMapper sysCodeMapper;

    private final Cache<Object, List<SysCodeDO>> sysCodeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    private final Cache<Object, Map<String, SysCodeDO>> sysCodeMapCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    @Override
    public List<SysCodeTreeVO> getSysCodeTreeVO(final SysCodeType sysCodeType) {
        List<SysCodeTreeVO> sysCodeTreeVOList = getSysCodeCache().stream()
                .filter(sysCodeDO -> sysCodeDO.getCodeType().equals(sysCodeType)
                        && isSysCodeDOValid(sysCodeDO))
                .map(sysCodeDO -> {
                    SysCodeTreeVO sysCodeTreeVO = new SysCodeTreeVO();
                    BeanUtils.copyProperties(sysCodeDO, sysCodeTreeVO);
                    return sysCodeTreeVO;
                }).collect(Collectors.toList());

        return TreeVO.getTree(sysCodeTreeVOList);
    }

    @Override
    public String getCodeNameByCodeValue(String codeValue) {
        SysCodeDO sysCodeDO = getSysCodeMapCache().get(codeValue);
        return sysCodeDO == null ? null : sysCodeDO.getCodeName();
    }

    @Override
    public List<SysCodeVO> getListByCodeValues(String codeValues) {
        if (StringUtils.isBlank(codeValues)) {
            return new ArrayList<>();
        }
        return getValidSortStreamByCodeValues(codeValues)
                .map(sysCodeDO -> {
                    SysCodeVO sysCodeVO = new SysCodeVO();
                    BeanUtils.copyProperties(sysCodeDO, sysCodeVO);
                    return sysCodeVO;
                }).collect(Collectors.toList());
    }

    @Override
    public String getStringNamesByCodeValues(String codeValues) {
        if (StringUtils.isBlank(codeValues)) {
            return "";
        }
        return getValidSortStreamByCodeValues(codeValues)
                .map(SysCodeDO::getCodeName)
                .collect(Collectors.joining(","));
    }

    @Override
    public SysCodeVO getCodeVOByCodeName(SysCodeType codeType, String codeName) {
        Optional<SysCodeDO> first = getSysCodeCache().stream()
                .filter(sysCodeDO -> sysCodeDO.getCodeType() == codeType
                        && sysCodeDO.getCodeName().equals(codeName)).findFirst();
        if (first.isPresent()) {
            SysCodeVO sysCodeVO = new SysCodeVO();
            BeanUtils.copyProperties(first.get(), sysCodeVO);
            return sysCodeVO;
        } else {
            return null;
        }
    }

    Stream<SysCodeDO> getValidSortStreamByCodeValues(String codeValues) {
        Map<String, SysCodeDO> sysCodeMapCache = getSysCodeMapCache();
        return Arrays.stream(codeValues.split(","))
                .map(sysCodeMapCache::get)
                .filter(Objects::nonNull)
                .filter(this::isSysCodeDOValid)
                .sorted(Comparator.comparing(SysCodeDO::getSort));
    }

    /**
     * 从缓存中获取所有的系统码值Map
     *
     * @return key：码值；value：sysCodeDO
     */
    private Map<String, SysCodeDO> getSysCodeMapCache() {
        try {
            return sysCodeMapCache.get(this, () ->
                    getSysCodeCache().stream().collect(Collectors.toMap(SysCodeDO::getCodeValue, value -> value))
            );
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从缓存中获取所有系统码值
     *
     * @return 系统码值集合
     */
    private List<SysCodeDO> getSysCodeCache() {
        try {
            return sysCodeCache.get(this, sysCodeMapper::getAllSysCodes);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查看系统码值是否有效
     *
     * @param sysCodeDO 系统码值
     * @return 是否有效
     */
    private boolean isSysCodeDOValid(SysCodeDO sysCodeDO) {
        return SysCodeStatus.VALID.equals(sysCodeDO.getCodeStatus());
    }
}

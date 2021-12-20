package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.entity.vos.TreeVO;
import cn.huacloud.taxpreference.common.enums.SysCodeStatus;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import cn.huacloud.taxpreference.services.common.entity.dtos.SysCodeQueryDTO;
import cn.huacloud.taxpreference.services.common.entity.dtos.SysCodeStringDTO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
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

    /**
     * All
     */
    private final Cache<Object, List<SysCodeDO>> sysCodeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    /**
     * SysCodeType -> List<SysCodeDO>
     */
    private final Cache<Object, Map<SysCodeType, List<SysCodeDO>>> sysCodeTypeMapCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    /**
     * PID -> List<SysCodeDO>
     */
    private final Cache<Object, Map<Long, List<SysCodeDO>>> sysCodePidMapCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    /**
     * SysCodeType -> List<SysCodeDO>
     */
    private final Cache<SysCodeType, Map<String, SysCodeDO>> typedCodeMapCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    @Override
    public List<SysCodeTreeVO> getSysCodeTreeVO(final SysCodeType sysCodeType) {
        List<SysCodeDO> sysCodeDOList = getSysCodeTypeMapCache().get(sysCodeType);
        if (sysCodeDOList == null) {
            return new ArrayList<>();
        }

        List<SysCodeTreeVO> sysCodeTreeVOList = sysCodeDOList.stream()
                .filter(this::isSysCodeDOValid)
                .map(sysCodeDO -> {
                    SysCodeTreeVO sysCodeTreeVO = new SysCodeTreeVO();
                    BeanUtils.copyProperties(sysCodeDO, sysCodeTreeVO);
                    return sysCodeTreeVO;
                }).collect(Collectors.toList());

        return TreeVO.getTree(sysCodeTreeVOList);
    }

    @Override
    public List<SysCodeTreeVO> getSysCodeTreeVO(SysCodeType sysCodeType, Predicate<SysCodeDO> filter) {
        List<SysCodeDO> sysCodeDOList = getSysCodeTypeMapCache().get(sysCodeType);
        if (sysCodeDOList == null) {
            return new ArrayList<>();
        }

        List<SysCodeTreeVO> sysCodeTreeVOList = sysCodeDOList.stream()
                .filter(this::isSysCodeDOValid)
                .filter(filter)
                .map(sysCodeDO -> {
                    SysCodeTreeVO sysCodeTreeVO = new SysCodeTreeVO();
                    BeanUtils.copyProperties(sysCodeDO, sysCodeTreeVO);
                    return sysCodeTreeVO;
                }).collect(Collectors.toList());

        return TreeVO.getTree(sysCodeTreeVOList);
    }

    @Override
    public String getCodeNameByCodeValue(String codeValue) {
        return "";
    }

    @Override
    public String getSysCodeName(SysCodeType sysCodeType, String codeValue) {
        SysCodeDO sysCodeDO = getSysCodeDO(sysCodeType, codeValue);
        if (sysCodeDO == null) {
            return "";
        }
        return sysCodeDO.getCodeName();
    }

    @Override
    public SysCodeDO getSysCodeDO(SysCodeType sysCodeType, String codeValue) {
        Map<String, SysCodeDO> codeMapByType = getCacheCodeMapByType(sysCodeType);
        return codeMapByType.get(codeValue);
    }

    @Override
    public SysCodeStringDTO getSysCodeStringDTO(Collection<String> codes, boolean withChildren) {
        SysCodeStringDTO sysCodeStringDTO = new SysCodeStringDTO();
        sysCodeStringDTO.setNames("");
        sysCodeStringDTO.setCodes("");
        return sysCodeStringDTO;
    }

    @Override
    public SysCodeStringDTO getSysCodeStringDTO(SysCodeType sysCodeType, Collection<String> codes, boolean withChildren) {
        SysCodeStringDTO stringDTO = new SysCodeStringDTO();
        stringDTO.setNames("");
        stringDTO.setCodes("");

        // is empty
        if (CollectionUtils.isEmpty(codes)) {
            return stringDTO;
        }

        Map<String, SysCodeDO> map = getSysCodeTypeMapCache().get(sysCodeType).stream()
                .filter(this::isSysCodeDOValid)
                .collect(Collectors.toMap(SysCodeDO::getCodeValue, value -> value));

        Set<String> targetCodes = new HashSet<>(codes);

        // 去除无效码值
        Set<String> keySet = map.keySet();
        targetCodes.retainAll(keySet);

        List<SysCodeDO> target;

        if (withChildren) {
            // 需要添加子节点
            Set<SysCodeDO> targetSet = new HashSet<>();
            Map<Long, List<SysCodeDO>> pidMap = map.values().stream().collect(Collectors.groupingBy(SysCodeDO::getPid));
            for (String targetCode : targetCodes) {
                setChildren(map.get(targetCode), targetSet, pidMap);
            }
            target = new ArrayList<>(targetSet);
        } else {
            // 不需要添加子节点
            target = targetCodes.stream()
                    .map(map::get)
                    .collect(Collectors.toList());
        }

        // 排序
        target.sort(Comparator.comparing(SysCodeDO::getSort));
        // 数据映射
        String names = target.stream().map(SysCodeDO::getCodeName).collect(Collectors.joining(","));
        String codeValues = target.stream().map(SysCodeDO::getCodeValue).collect(Collectors.joining(","));
        stringDTO.setNames(names);
        stringDTO.setCodes(codeValues);
        return stringDTO;
    }

    /**
     * 设置子节点到 targetSet中
     */
    private void setChildren(SysCodeDO sysCodeDO, Set<SysCodeDO> targetSet, Map<Long, List<SysCodeDO>> pidMap) {
        if (sysCodeDO == null) {
            return;
        }
        targetSet.add(sysCodeDO);
        List<SysCodeDO> sysCodeDOList = pidMap.get(sysCodeDO.getId());
        if (sysCodeDOList == null) {
            return;
        }
        for (SysCodeDO child : sysCodeDOList) {
            setChildren(child, targetSet, pidMap);
        }
    }

    @Override
    public SysCodeVO getCodeVOByCodeName(SysCodeType codeType, String codeName) {
        Optional<SysCodeDO> first = getCacheSysCodes().stream()
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

    @Override
    public List<SysCodeSimpleVO> getSimpleVOListByCodeValues(String codeValues) {
        return new ArrayList<>();
    }

    @Override
    public List<SysCodeSimpleVO> getSimpleVOListByCodeValues(SysCodeType sysCodeType, String codeValues) {
        if (StringUtils.isBlank(codeValues)) {
            return new ArrayList<>();
        }
        return getValidSortStreamByCodeValues(sysCodeType, codeValues)
                .map(sysCodeDO -> new SysCodeSimpleVO()
                        .setCodeName(sysCodeDO.getCodeName())
                        .setCodeValue(sysCodeDO.getCodeValue()))
                .collect(Collectors.toList());
    }

    @Override
    public SysCodeSimpleVO getSimpleVOByCode(String codeValue) {
        /*SysCodeDO sysCodeDO = getSysCodeMapCache().get(codeValue);
        if (sysCodeDO == null) {
            return new SysCodeSimpleVO();
        }
        return new SysCodeSimpleVO().setCodeName(sysCodeDO.getCodeName())
                .setCodeValue(sysCodeDO.getCodeValue());*/
        return new SysCodeSimpleVO();
    }

    @Override
    public SysCodeSimpleVO getSimpleVOByCode(SysCodeType sysCodeType, String codeValue) {
        Map<String, SysCodeDO> codeMapByType = getCacheCodeMapByType(sysCodeType);
        SysCodeDO sysCodeDO = codeMapByType.get(codeValue);
        if (sysCodeDO == null) {
            return new SysCodeSimpleVO();
        }
        return new SysCodeSimpleVO().setCodeName(sysCodeDO.getCodeName())
                .setCodeValue(sysCodeDO.getCodeValue());
    }

    @Override
    public List<SysCodeDO> getSysCodeDOByCodeType(SysCodeType sysCodeType) {
        return getCacheSysCodes().stream()
                .filter(this::isSysCodeDOValid)
                .sorted(Comparator.comparing(SysCodeDO::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> withChildrenCodes(Collection<?> target) {
        return new ArrayList<>();
    }

    @Override
    public List<String> withChildrenCodes(SysCodeType sysCodeType, Collection<?> target) {
        Map<String, SysCodeDO> sysCodeDOMap = getCacheCodeMapByType(sysCodeType);
        List<SysCodeDO> targetCodes = target.stream()
                .map(sysCodeDOMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, List<SysCodeDO>> sysCodePidMapCache = getSysCodePidMapCache();
        Set<SysCodeDO> targetSet = new HashSet<>();
        for (SysCodeDO targetCode : targetCodes) {
            setChildren(targetCode, targetSet, sysCodePidMapCache);
        }
        return targetSet.stream().map(SysCodeDO::getCodeValue).collect(Collectors.toList());
    }

    @Override
    public List<SysCodeTreeVO> getSysCodeTreeVOLazy(SysCodeQueryDTO sysCodeQueryDTO) {
        List<SysCodeDO> sysCodeDOList = getSysCodeTypeMapCache().get(sysCodeQueryDTO.getSysCodeType());
        if (sysCodeDOList == null) {
            return new ArrayList<>();
        }
        return sysCodeDOList.stream()
                .filter(this::isSysCodeDOValid)
                .filter(sysCodeDO -> sysCodeQueryDTO.getPid().equals(sysCodeDO.getPid()))
                .map(sysCodeDO -> {
                    SysCodeTreeVO sysCodeTreeVO = new SysCodeTreeVO();
                    BeanUtils.copyProperties(sysCodeDO, sysCodeTreeVO);
                    return sysCodeTreeVO;
                }).collect(Collectors.toList());
    }

    Stream<SysCodeDO> getValidSortStreamByCodeValues(SysCodeType sysCodeType, String codeValues) {
        Map<String, SysCodeDO> sysCodeMapCache = getCacheCodeMapByType(sysCodeType);
        return Arrays.stream(codeValues.split(","))
                .map(sysCodeMapCache::get)
                .filter(Objects::nonNull)
                .filter(this::isSysCodeDOValid)
                .sorted(Comparator.comparing(SysCodeDO::getSort));
    }

    /**
     * 从缓存中获取所有的系统码值TypeMap
     *
     * @return key：码值类型；value：sysCodeDO
     */
    private Map<SysCodeType, List<SysCodeDO>> getSysCodeTypeMapCache() {
        try {
            return sysCodeTypeMapCache.get(this, () -> {
                        Map<SysCodeType, List<SysCodeDO>> map = getCacheSysCodes().stream().collect(Collectors.groupingBy(SysCodeDO::getCodeType));
                        for (Map.Entry<SysCodeType, List<SysCodeDO>> entry : map.entrySet()) {
                            entry.getValue().sort(Comparator.comparing(SysCodeDO::getSort));
                        }
                        return map;
                    }
            );
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从缓存中获取所有的系统码值TypeMap
     *
     * @return key：码值类型；value：sysCodeDO
     */
    private Map<Long, List<SysCodeDO>> getSysCodePidMapCache() {
        try {
            return sysCodePidMapCache.get(this, () -> getCacheSysCodes().stream().collect(Collectors.groupingBy(SysCodeDO::getPid)));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取类型下的系统码值map
     *
     * @param sysCodeType 系统码值类型
     * @return map key -> codeValue, value -> SysCodeDO
     */
    private Map<String, SysCodeDO> getCacheCodeMapByType(SysCodeType sysCodeType) {
        try {
            return typedCodeMapCache.get(sysCodeType, () -> {
                Map<SysCodeType, List<SysCodeDO>> sysCodeTypeMapCache = getSysCodeTypeMapCache();
                List<SysCodeDO> sysCodeDOList = sysCodeTypeMapCache.get(sysCodeType);
                Map<String, SysCodeDO> map = new HashMap<>();
                if (CollectionUtils.isEmpty(sysCodeDOList)) {
                    return map;
                }
                for (SysCodeDO sysCodeDO : sysCodeDOList) {
                    String codeValue = sysCodeDO.getCodeValue();
                    if (map.containsKey(codeValue)) {
                        log.error("类型为：{}，的系统码值重复：{}", sysCodeDO.getCodeType().getValue(), codeValue);
                        continue;
                    }
                    map.put(codeValue, sysCodeDO);
                }
                return map;
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从缓存中获取所有系统码值
     *
     * @return 系统码值集合
     */
    private List<SysCodeDO> getCacheSysCodes() {
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

    @Override
    public void clear() {
        sysCodeCache.invalidateAll();
        sysCodeTypeMapCache.invalidateAll();
        sysCodePidMapCache.invalidateAll();
        typedCodeMapCache.invalidateAll();
    }
}

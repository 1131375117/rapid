package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.enums.SysParamStatus;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.handler.param.SysParamHandler;
import cn.huacloud.taxpreference.services.common.mapper.SysParamMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class SysParamServiceImpl implements SysParamService {

    private final SysParamMapper sysParamMapper;

    private final ObjectMapper objectMapper;
    /**
     * key -> sysParamType， value -> List<SysParamDO>
     */
    private final Cache<String, List<SysParamDO>> sysParamTypeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    private final List<SysParamHandler<?, ?>> handlers;

    @Override
    public SysParamDO getSysParamDO(String paramType, String paramKey) {
        LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class)
                .eq(SysParamDO::getParamKey, paramKey)
                .eq(!StringUtils.isEmpty(paramType), SysParamDO::getParamType, paramType)
                .eq(SysParamDO::getParamStatus, SysParamStatus.VALID);
        List<SysParamDO> sysParamDOS = sysParamMapper.selectList(queryWrapper);
        if (sysParamDOS.isEmpty()) {
            return null;
        }
        return sysParamDOS.iterator().next();
    }

    @Override
    public <T> T getObjectParamByTypes(List<String> sysParamTypes, Class<T> clazz) {
        // 返回空对象
        T emptyObject;
        try {
            emptyObject = clazz.newInstance();
        } catch (Exception e) {
            log.error("系统参数获取对象参数失败", e);
            return null;
        }
        // 判断类型是否为空
        if (CollectionUtils.isEmpty(sysParamTypes)) {
            return emptyObject;
        }
        try {
            List<SysParamDO> sysParamDOList = getCacheSysParamByTypes(sysParamTypes.toArray(new String[0]));
            Map<Object, Object> tempMap = new HashMap<>();
            Map<String, Integer> indexMap = getIndexMap(sysParamTypes);
            sysParamDOList.sort(Comparator.comparingInt(a -> indexMap.get(a.getParamType())));
            setPropertyToMap(tempMap, sysParamDOList);
            String str = objectMapper.writeValueAsString(tempMap);
            return objectMapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            log.error("系统参数JSON反序列化失败:", e);
            return emptyObject;
        }
    }

    @Override
    public <T> Map<String, T> getMapParamByTypes(Class<T> clazz, String... sysParamTypes) {

        List<SysParamDO> sysParamDOList = getCacheSysParamByTypes(sysParamTypes);

        Map<String, Integer> indexMap = getIndexMap(Arrays.asList(sysParamTypes));
        sysParamDOList.sort(Comparator.comparingInt(a -> indexMap.get(a.getParamType())));

        Map<Object, Object> tempMap = new LinkedHashMap<>();
        setPropertyToMap(tempMap, sysParamDOList);

        try {
            String str = objectMapper.writeValueAsString(tempMap);
            MapType mapType = objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, clazz);
            return objectMapper.readValue(str, mapType);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException:", e);
            return new LinkedHashMap<>();
        }
    }

    @Override
    public <T> T getSingleParamValue(String sysParamType, String sysParamKey, Class<T> clazz) {
        List<SysParamDO> sysParamDOList = getCacheSysParamByTypes(sysParamType);
        if (sysParamKey != null) {
            // 参数Key过滤
            sysParamDOList = sysParamDOList.stream().filter(sysParamDO -> sysParamKey.equals(sysParamDO.getParamKey())).collect(Collectors.toList());
        }
        String target;
        if (CollectionUtils.isEmpty(sysParamDOList)) {
            return null;
        }
        // 反序列化value
        List<String> wrapper = new ArrayList<>();
        wrapper.add(sysParamDOList.iterator().next().getParamValue());
        try {
            target = objectMapper.writeValueAsString(wrapper);
            CollectionLikeType type = objectMapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, clazz);
            List<T> readValue = objectMapper.readValue(target, type);
            return readValue.iterator().next();
        } catch (JsonProcessingException e) {
            log.error("转换异常:", e);
            return null;
        }
    }

    @Override
    public <T, R> R getParamByHandler(Class<SysParamHandler<T, R>> handlerClass, T handlerParam, List<String> paramTypes) {
        Optional<SysParamHandler<?, ?>> optional = handlers.stream()
                .filter(handler -> handlerClass.isAssignableFrom(handler.getClass()))
                .findFirst();
        if (!optional.isPresent()) {
            return null;
        }
        SysParamHandler<T, R> handler = (SysParamHandler<T, R>) optional.get();
        if (paramTypes.isEmpty()) {
            log.error("参数处理器的参数类型不能为空");
            return null;
        }
        List<SysParamDO> sysParamDOS = getCacheSysParamByTypes(paramTypes.toArray(new String[0]));
        return handler.handle(sysParamDOS, handlerParam);
    }


    /**
     * 通过系统参数类型从缓存中获取系统参数
     *
     * @param paramTypes 系统参数类型
     * @return 系统参数集合
     */
    private List<SysParamDO> getCacheSysParamByTypes(String... paramTypes) {
        List<SysParamDO> merge = new ArrayList<>();
        try {
            for (String paramType : paramTypes) {
                List<SysParamDO> sysParamDOS = sysParamTypeCache.get(paramType, () -> sysParamMapper.getSysParamDOListByType(paramType));
                merge.addAll(sysParamDOS);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return merge;
    }

    private Map<String, Integer> getIndexMap(List<String> sysParamTypes) {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < sysParamTypes.size(); i++) {
            String key = sysParamTypes.get(i);
            if (indexMap.containsKey(key)) {
                continue;
            }
            indexMap.put(key, i);
        }
        return indexMap;
    }

    private void setPropertyToMap(Map<Object, Object> map, List<SysParamDO> sysParamDOList) {
        for (SysParamDO sysParamDO : sysParamDOList) {
            String paramKey = sysParamDO.getParamKey();
            if (paramKey.contains(".")) {
                paramKey = StringUtils.substringAfterLast(paramKey, ".");
            }
            String paramValue = sysParamDO.getParamValue();
            if (map.containsKey(paramKey)) {
                continue;
            }
            map.put(paramKey, paramValue);
        }
    }

    @Override
    public void clear() {
        sysParamTypeCache.invalidateAll();
    }
}

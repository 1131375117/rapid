package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.mapper.SysParamMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class SysParamServiceImpl implements SysParamService {

    private final SysParamMapper sysParamMapper;
    private final ObjectMapper objectMapper;

    private final Cache<Object, List<SysParamDO>> sysParamCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    @Override
    public SysParamDO selectByParamKey(String paramKey) {
        LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class)
                .eq(SysParamDO::getParamKey, paramKey);
        SysParamDO sysParamDO = sysParamMapper.selectOne(queryWrapper);
        return sysParamDO;
    }

    @Override
    public <T> T getObjectParamByTypes(List<String> sysParamTypes, Class<T> clazz) {
        HashMap<Object, Object> objectHashMap = new HashMap<>();
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < sysParamTypes.size(); i++) {
            indexMap.put(sysParamTypes.get(i), i);
        }
        if (!CollectionUtils.isEmpty(sysParamTypes)) {
            try {
                //根据类型获取缓存

                List<SysParamDO> sysParamDOList = sysParamMapper.getSysParamDOList(sysParamTypes);
                sysParamDOList.sort(Comparator.comparingInt(a -> indexMap.get(a.getParamType())));
                getMap(objectHashMap, sysParamDOList);
                String str = objectMapper.writeValueAsString(objectHashMap);
                return objectMapper.readValue(str, clazz);
            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException:", e);
            }
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("类实例化异常:", e);

        }
        return (T) objectHashMap;
    }

    @Override
    public <T> Map<String, T> getMapParamByTypes(Class<T> clazz, String... args) {
        HashMap<Object, Object> map = new HashMap<>();
        Map<String, Integer> indexMap = new HashMap<>();
        List<SysParamDO> sysParamDOList = sysParamMapper.getSysParamDOList(args);

        for (int i = 0; i < args.length; i++) {
            indexMap.put(args[0], i);
        }
        sysParamDOList.sort(Comparator.comparingInt(a -> indexMap.get(a.getParamType())));

        getMap(map, sysParamDOList);
        try {
            String str = objectMapper.writeValueAsString(map);
            MapType mapType = objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, clazz);
            return objectMapper.readValue(str, mapType);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException:", e);
        }
        return new HashMap<>();

    }

    @Override
    public <T> T getSingleParamValue(String sysParamType, String sysParamKey, Class<T> clazz) {
        return null;
    }

    private void getMap(HashMap<Object, Object> map, List<SysParamDO> sysParamDOList) {
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
}

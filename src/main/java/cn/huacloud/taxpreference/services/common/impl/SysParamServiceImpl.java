package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.mapper.SysParamMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class SysParamServiceImpl implements SysParamService {

    private final SysParamMapper sysParamMapper;
    private final ObjectMapper objectMapper;

    @Override
    public SysParamDO selectByParamKey(String paramKey) {
        LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class)
                .eq(SysParamDO::getParamKey, paramKey);
        SysParamDO sysParamDO = sysParamMapper.selectOne(queryWrapper);
        return sysParamDO;
    }

    @Override
    public <T> T getObjectParamByTypes(List<String> sysParamTypes, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        HashMap<Object, Object> objectHashMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(sysParamTypes)) {
            try {
                LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class);
                queryWrapper.eq(SysParamDO::getParamStatus, "VALID");
                queryWrapper.in(SysParamDO::getParamType, sysParamTypes);
                List<SysParamDO> sysParamDOList = sysParamMapper.selectList(queryWrapper);
                for (SysParamDO sysParamDO : sysParamDOList) {
                    String paramKey = sysParamDO.getParamKey();
                    if (paramKey.contains(".")) {
                        paramKey = StringUtils.substringAfterLast(paramKey, ".");
                    }
                    String paramValue = sysParamDO.getParamValue();
                    if (objectHashMap.containsKey(paramKey)) {
                        continue;
                    }
                    objectHashMap.put(paramKey, paramValue);
                }
                String str = objectMapper.writeValueAsString(objectHashMap);
                return objectMapper.readValue(str, clazz);
            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException:", e);
            }
        }
        return clazz.newInstance();
    }

    @Override
    public <T> Map<String, T> getMapParamByTypes(Class<T> clazz, String... args) throws JsonProcessingException {
        HashMap<Object, Object> map = new HashMap<>();

        List<SysParamDO> sysParamDOList = sysParamMapper.getSysParamDOList(args);
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
        try {
            String str = objectMapper.writeValueAsString(map);
            MapType mapType = objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, clazz);
            return objectMapper.readValue(str, mapType);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException:", e);
        }
        return null;

    }
}

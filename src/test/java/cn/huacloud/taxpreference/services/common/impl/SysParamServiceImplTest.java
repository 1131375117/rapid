package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.Param;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.common.mapper.SysParamMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SysParamServiceImplTest {
    static SysParamService sysParamService;
    static ObjectMapper objectMapper;
    static ArrayList<SysParamDO> sysParamDOArrayList;



    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        sysParamDOArrayList = new ArrayList<>();
        SysParamDO sysParamDO = new SysParamDO().setParamKey("views.taxPreference").setParamValue("TAX_PREFERENCE");
        sysParamDOArrayList.add(sysParamDO);
        ObjectMapper configure = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SysParamMapper sysParamMapper = mock(SysParamMapper.class);
        LambdaQueryWrapper<SysParamDO> queryWrapper = Wrappers.lambdaQuery(SysParamDO.class);
        queryWrapper.eq(SysParamDO::getParamStatus, "VALID");
        queryWrapper.in(SysParamDO::getParamType, "operation.views");
        when(sysParamMapper.selectList(queryWrapper)).thenReturn(sysParamDOArrayList);
        sysParamService = new SysParamServiceImpl(sysParamMapper, configure);

    }

    @Test
    void getObjectParamByTypes() {
        ArrayList<String> sysparamList = new ArrayList<>();
        sysparamList.add("operation.views");
        try {
            Param objectParamByTypes = sysParamService.getObjectParamByTypes(sysparamList, Param.class);
            System.out.println(objectParamByTypes);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getMapParamByTypes() {
        try {
            Map<String, DocType> mapParamByTypes = sysParamService.getMapParamByTypes(DocType.class, "operation.views");
            System.out.println(mapParamByTypes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Test
    void getMapParamByTypes1() {
        HashMap<Object, Object> map = new HashMap<>();

        List<SysParamDO> sysParamDOList = sysParamDOArrayList;
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
            MapType mapType = objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, DocType.class);
            System.out.println(objectMapper.readValue(str, mapType)+"---");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
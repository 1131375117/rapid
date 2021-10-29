package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.entity.vos.TreeVO;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;
import cn.huacloud.taxpreference.services.common.mapper.SysCodeMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                .filter(sysCodeDO -> sysCodeDO.getCodeType().equals(sysCodeType))
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

    private Map<String, SysCodeDO> getSysCodeMapCache() {
        try {
            return sysCodeMapCache.get(this, () ->
                getSysCodeCache().stream().collect(Collectors.toMap(SysCodeDO::getCodeValue, value -> value))
            );
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SysCodeDO> getSysCodeCache() {
        try {
            return sysCodeCache.get(this, sysCodeMapper::getAllValid);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


}

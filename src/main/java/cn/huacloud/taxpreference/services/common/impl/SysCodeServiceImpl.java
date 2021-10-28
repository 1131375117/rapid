package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.entity.vos.TreeVO;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysCodeDO;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeTreeVO;
import cn.huacloud.taxpreference.services.common.mapper.SysCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统码值服务实现
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class SysCodeServiceImpl implements SysCodeService {

    private final SysCodeMapper sysCodeMapper;

    @Override
    public List<SysCodeTreeVO> getSysCodeTreeVO(SysCodeType sysCodeType) {
        List<SysCodeDO> sysCodeDOList = sysCodeMapper.getSysCodeDOByType(sysCodeType);
        List<SysCodeTreeVO> list = sysCodeDOList.stream().map(sysCodeDO -> {
            SysCodeTreeVO sysCodeVO = new SysCodeTreeVO();
            BeanUtils.copyProperties(sysCodeDO, sysCodeVO);
            return sysCodeVO;
        }).collect(Collectors.toList());
        return TreeVO.getTree(list);
    }
}

package cn.huacloud.taxpreference.sync.spider.processor;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class SysCodeProcessors {

    @Autowired
    private SysCodeService sysCodeService;

    public static final String BLANK_SPACE = " ";

    public Function<String, SysCodeVO> area = name -> {
        if (StringUtils.isNotBlank(name) && name.contains(BLANK_SPACE)) {
            name = StringUtils.substringAfterLast(name, BLANK_SPACE);
        }
        SysCodeVO sysCodeVO = sysCodeService.getCodeVOByCodeName(SysCodeType.AREA, name);
        if (sysCodeVO == null) {
            return new SysCodeVO();
        }
        return sysCodeVO;
    };
}

package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.BaseApplicationTest;
import cn.huacloud.taxpreference.services.common.entity.dtos.SysCodeStringDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SysCodeServiceTest extends BaseApplicationTest {

    @Autowired
    SysCodeService sysCodeService;

    @Test
    public void getSysCodeStringDTO() {
        List<String> codes = Arrays.asList("INDUSTRY_B", "INDUSTRY_09", "ENTERPRISE_TYPE_GTGSH");
        SysCodeStringDTO one = sysCodeService.getSysCodeStringDTO(codes, false);
        SysCodeStringDTO two = sysCodeService.getSysCodeStringDTO(codes, true);
        log.info("");
    }
}
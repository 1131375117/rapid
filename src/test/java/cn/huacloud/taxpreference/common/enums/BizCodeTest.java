package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class BizCodeTest {

    @Test
    public void getResultVO() {
        ResultVO<Void> vo = BizCode._4201.getResultVO("xxx");
        log.info(vo.toString());
    }


}
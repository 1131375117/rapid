package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.BaseApplicationTest;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.SysParamService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fuhua
 **/
public class SingleTest extends BaseApplicationTest {
    @Autowired
    SysParamService sysParamService;

    @Test
    public void getObjectParamByTypes() {

        DocType singleParamValue = sysParamService.getSingleParamValue("fuhua", "fuhua", DocType.class);
        System.out.println(singleParamValue.getName());
    }


}

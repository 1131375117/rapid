package cn.huacloud.taxpreference.common.utils;

import org.junit.Test;

public class DocCodeUtilTest {

    @Test
    public void getDocCode() {
        String docWordCode = "财税";
        String docYearCode = "2019";
        Integer docNumCode = 56;

        String docCode01 = DocCodeUtil.getDocCode(null, null, null);
        String docCode02 = DocCodeUtil.getDocCode(docWordCode, null, null);
        String docCode03 = DocCodeUtil.getDocCode(docWordCode, docYearCode, null);
        String docCode04 = DocCodeUtil.getDocCode(docWordCode, null, docNumCode);
        String docCode05 = DocCodeUtil.getDocCode(docWordCode, docYearCode, docNumCode);
    }
}
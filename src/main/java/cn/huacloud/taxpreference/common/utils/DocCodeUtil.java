package cn.huacloud.taxpreference.common.utils;

import cn.huacloud.taxpreference.services.producer.entity.vos.DocCodeVO;
import org.apache.commons.lang3.StringUtils;

/**
 * 文号工具类
 * @author wangkh
 */
public class DocCodeUtil {

    /**
     * 获取拼接的文号
     * @param docWordCode 字号
     * @param docYearCode 年号
     * @param docNumCode 序号
     * @return 拼接后的文号
     */
    public static String getDocCode(String docWordCode, Integer docYearCode, Integer docNumCode) {
        StringBuilder sb = new StringBuilder("");
        if (StringUtils.isNotBlank(docWordCode)) {
            sb.append(docWordCode);
        }
        if (docYearCode != null) {
            sb.append("〔").append(docYearCode).append("〕").append("年");
        }
        if (docNumCode != null) {
            sb.append(docNumCode).append("号");
        }
        return sb.toString();
    }

    public static String getDocCode(DocCodeVO docCodeVO) {
        return getDocCode(docCodeVO.getDocWordCode(), docCodeVO.getDocYearCode(), docCodeVO.getDocNumCode());
    }
}

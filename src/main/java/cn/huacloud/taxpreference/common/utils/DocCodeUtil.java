package cn.huacloud.taxpreference.common.utils;

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
    public static String getDocCode(String docWordCode, String docYearCode, Integer docNumCode) {
        StringBuilder sb = new StringBuilder("");
        if (docWordCode != null) {
            sb.append(docWordCode);
        }
        if (docYearCode != null) {
            sb.append("〔").append(docYearCode).append("〕");
        }
        if (docNumCode != null) {
            sb.append(docNumCode).append("号");
        }
        return sb.toString();
    }
}
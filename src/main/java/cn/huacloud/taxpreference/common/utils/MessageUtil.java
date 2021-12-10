package cn.huacloud.taxpreference.common.utils;

/**
 * 消息工具类
 * @author wangkh
 */
public class MessageUtil {

    /**
     * 获取6位长随机字符验证码
     * @return 6位长的数字验证码
     */
    public static String getRandomVerificationCode() {
        String str = Math.random() + "";
        if (str.length() < 8) {
            str += "24681357";
        }
        return str.substring(2, 8);
    }
}

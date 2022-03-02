package cn.huacloud.taxpreference.services.wwx;

/**
 * @author wangkh
 */
public interface TaxPreferenceWWXService  extends WWXService {
    @Override
    default String getAppName() {
        return "税小秘";
    }
}

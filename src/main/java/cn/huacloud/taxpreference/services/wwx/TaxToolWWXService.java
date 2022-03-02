package cn.huacloud.taxpreference.services.wwx;

/**
 * @author wangkh
 */
public interface TaxToolWWXService extends WWXService {
    @Override
    default String getAppName() {
        return "税务小工具";
    }
}

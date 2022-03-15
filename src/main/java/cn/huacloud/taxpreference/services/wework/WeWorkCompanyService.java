package cn.huacloud.taxpreference.services.wework;

import cn.huacloud.taxpreference.services.wework.entity.dos.WeWorkCompanyDO;

/**
 * @author wangkh
 */
public interface WeWorkCompanyService {

    void save(WeWorkCompanyDO weWorkCompanyDO);

    void deleteByCorpIdAndAgentId(String corpId, String suiteId);
}

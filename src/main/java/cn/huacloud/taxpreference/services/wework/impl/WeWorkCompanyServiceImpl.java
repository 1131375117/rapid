package cn.huacloud.taxpreference.services.wework.impl;

import cn.huacloud.taxpreference.services.wework.WeWorkCompanyService;
import cn.huacloud.taxpreference.services.wework.entity.dos.WeWorkCompanyDO;
import cn.huacloud.taxpreference.services.wework.mapper.WeWorkCompanyMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Service
public class WeWorkCompanyServiceImpl implements WeWorkCompanyService {
    @Autowired
    private WeWorkCompanyMapper weWorkCompanyMapper;

    @Transactional
    @Override
    public void save(WeWorkCompanyDO weWorkCompanyDO) {
        if (StringUtils.isBlank(weWorkCompanyDO.getCorpId()) || StringUtils.isBlank(weWorkCompanyDO.getSuiteId())) {
            return;
        }

        // 使用corpId和agentId定位公司是否已存在
        WeWorkCompanyDO findOne = weWorkCompanyMapper.findByCropIdAndAgentId(weWorkCompanyDO.getCorpId(), weWorkCompanyDO.getSuiteId());
        if (findOne != null) {
            return;
        }

        weWorkCompanyDO.setCreateTime(LocalDateTime.now())
                .setDeleted(false);

        weWorkCompanyMapper.insert(weWorkCompanyDO);
    }

    @Override
    public void deleteByCorpIdAndAgentId(String corpId, String suiteId) {
        WeWorkCompanyDO companyDO = weWorkCompanyMapper.findByCropIdAndAgentId(corpId, suiteId);
        if (companyDO == null) {
            return;
        }
        companyDO.setDeleted(true);
        companyDO.setUpdateTime(LocalDateTime.now());
        weWorkCompanyMapper.updateById(companyDO);
    }
}

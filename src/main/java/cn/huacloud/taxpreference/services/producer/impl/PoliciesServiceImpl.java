package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.services.producer.PoliciesService;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wuxin
 */
@Service
public class PoliciesServiceImpl implements PoliciesService {

    @Autowired
    private PoliciesMapper policiesMapper;

    /**
     * 政策列表查询
     *
     * @param policiesDO
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<PoliciesDO> getPolices(PoliciesDO policiesDO, Integer page, Integer size) {
        return null;
    }

    /**
     * 新增政策法规
     *
     * @param policiesDO
     */
    @Override
    public void insertPolicies(PoliciesDO policiesDO) {
        //新增政策法规
        policiesMapper.insert(policiesDO);
    }

    /**
     * 根据政策法规id获取详细信息
     *
     * @param id
     * @return
     */
    @Override
    public PoliciesDO getPoliciesById(Long id) {
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        return policiesDO;
    }

    /**
     * 修改政策法规
     *
     * @param policiesDO
     */
    @Override
    public void updatePolicies(PoliciesDO policiesDO) {
        //获取当前政策法规的id
        Long policiesDOId = policiesDO.getId();
        //根据id进行查询
        policiesMapper.selectById(policiesDOId);
        //修改政策法规
        policiesMapper.updateById(policiesDO);
    }
}

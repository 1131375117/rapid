package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.taxpreference.SortType;
import cn.huacloud.taxpreference.common.enums.taxpreference.TaxPreferenceStatus;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.SubmitConditionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPreferencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.SubmitConditionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferencePoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPreferencesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.SubmitConditionVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferencePoliciesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceVO;
import cn.huacloud.taxpreference.services.producer.mapper.ProcessServiceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.SubmitConditionMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferencePoliciesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 优惠政策服务实现类
 * @author: fuhua
 * @create: 2021-10-21 10:36
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class TaxPreferenceServiceImpl implements TaxPreferenceService {

    private final TaxPreferenceMapper taxPreferenceMapper;

    private final TaxPreferencePoliciesMapper taxPreferencePoliciesMapper;

    private final SubmitConditionMapper submitConditionMapper;

    private final ProcessServiceMapper ProcessServiceMapper;

     static final String TAX_PREFERENCE_ID ="tax_preference_id";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> insertTaxPreference(TaxPreferenceDTO taxPreferenceDTO) {
        //检查优惠事项名称是否存在
        judgeExists( taxPreferenceDTO.getTaxPreferenceName());

        //新增-税收优惠表t_tax_preference
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(taxPreferenceDTO);
        taxPreferenceDO.setCreateTime(LocalDateTime.now());

        log.info("税收优惠新增对象:{}",taxPreferenceDO);
        taxPreferenceMapper.insert(taxPreferenceDO);

        //新增-税收优惠政策法规关联表t_tax_preference_ policies
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOList = getTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
        for (TaxPreferencePoliciesDO taxPreferencePoliciesDO : taxPreferencePoliciesDOList) {
            taxPreferencePoliciesMapper.insert(taxPreferencePoliciesDO);
        }

        //新增-申报条件表 t_submit_condition
        insertSubmitConditionDOs(taxPreferenceDTO, taxPreferenceDO);
        return ResultVO.ok();
    }

    @NotEmpty(message = "优惠事项名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String getTaxPreferenceName(TaxPreferenceDTO taxPreferenceDTO) {
        return taxPreferenceDTO.getTaxPreferenceName();
    }
    /**
     * 判断此税收优惠事项是否存在
     * */
    private Boolean judgeExists(String name) {
        LambdaQueryWrapper<TaxPreferenceDO> queryWrapper = Wrappers.lambdaQuery(TaxPreferenceDO.class)
                .eq(TaxPreferenceDO::getTaxPreferenceName, name)
                .eq(TaxPreferenceDO::isDeleted,0)
                ;
        Long count = taxPreferenceMapper.selectCount(queryWrapper);
        if(count>0){
            throw BizCode._4302.exception();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> updateTaxPreference(TaxPreferenceDTO taxPreferenceDTO) {
        judgeExists(taxPreferenceDTO.getTaxPreferenceName());
        //修改-税收优惠表t_tax_preference
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(taxPreferenceDTO);
        taxPreferenceMapper.updateById(taxPreferenceDO);

        //修改-税收优惠政策法规关联表t_tax_preference_ policies
        List<TaxPreferencePoliciesDO> preferencePoliciesDOList = getTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
        for (TaxPreferencePoliciesDO preferencePoliciesDO : preferencePoliciesDOList) {
            taxPreferencePoliciesMapper.updateByTaxPreferenceId(preferencePoliciesDO);
        }

        //修改-申报条件表 t_submit_condition
        updateSubmitConditionByTaxPreferenceId(taxPreferenceDTO);
        return ResultVO.ok();
    }

    @Override
    public ResultVO<TaxPreferenceVO> queryTaxPreferenceInfo(Long id) {
        TaxPreferenceVO taxPreferenceVO = new TaxPreferenceVO();
        Map<String, Object> columnMap = new HashMap<>(16);
        columnMap.put(TAX_PREFERENCE_ID, id);

        //根据id查询税收优惠表t_tax_preference信息
        TaxPreferenceDO taxPreferenceDO = taxPreferenceMapper.selectById(id);
        BeanUtils.copyProperties(taxPreferenceDO, taxPreferenceVO);

        //查询政策信息
        List<TaxPreferencePoliciesVO> taxPreferencePoliciesVOList = getTaxPreferencePoliciesVOS(columnMap);
        taxPreferenceVO.setTaxPreferencePoliciesVOList(taxPreferencePoliciesVOList);

        //查询申报信息
        List<SubmitConditionDO> submitConditionDOList = submitConditionMapper.selectByMap(columnMap);
        List<SubmitConditionVO> submitConditionVOList = getSubmitConditionVOS(submitConditionDOList);
        taxPreferenceVO.setSubmitConditionVOList(submitConditionVOList);

        return ResultVO.ok(taxPreferenceVO);
    }

    @Override
    public ResultVO<PageVO<QueryTaxPreferencesVO>> queryTaxPreferenceList(QueryTaxPreferencesDTO queryTaxPreferencesDTO, Long userId) {
        log.info("查询条件:queryTaxPreferencesDTO:{}",queryTaxPreferencesDTO);
        queryTaxPreferencesDTO.paramReasonable();
        Page<QueryTaxPreferencesVO> page = new Page<>(queryTaxPreferencesDTO.getPageNum(), queryTaxPreferencesDTO.getPageSize());
        //获取排序字段
        String sort = getSort(queryTaxPreferencesDTO);
        IPage<QueryTaxPreferencesVO> iPage = taxPreferenceMapper.queryTaxPreferenceVOList(page, queryTaxPreferencesDTO,sort,userId);
        List<QueryTaxPreferencesVO> records = iPage.getRecords();
        records.forEach(queryTaxPreferencesVO -> {
           String  processStatus = ProcessServiceMapper.selectByTaxPreferenceId(queryTaxPreferencesVO.getId());
           queryTaxPreferencesVO.setProcessStatus(processStatus);
        });
        PageVO<QueryTaxPreferencesVO> pageVO = PageVO.createPageVO(iPage, iPage.getRecords());
        return ResultVO.ok(pageVO);
    }

    /**
     * 获取排序字段
     * @param queryTaxPreferencesDTO
     */
    private String getSort(QueryTaxPreferencesDTO queryTaxPreferencesDTO) {
        String sort=SortType.CREATE_TIME.getValue();
        if(queryTaxPreferencesDTO.getSortType().equals(SortType.UPDATE_TIME)){
            sort=SortType.UPDATE_TIME.name();
        }
        log.info("排序字段sort:{}",sort);
        return sort;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> deleteTaxPreference(Long[] ids) {
        for (Long id : ids) {
            log.info("删除条件:ids={}",id);
            Map<String, Object> keyMap=new HashMap<>(16);
            keyMap.put(TAX_PREFERENCE_ID,id);

            //逻辑删除t_tax_preference
            taxPreferenceMapper.updateDeletedById(id);
            //删除-申报条件t_submit_condition
            submitConditionMapper.deleteByMap(keyMap);
            //删除政策法规t_tax_preference_policies
            taxPreferencePoliciesMapper.deleteByMap(keyMap);
            log.info("id为{}的税收优惠删除成功！",id);
        }
        return ResultVO.ok(null);
    }

    @Override
    public ResultVO<Void> reTaxPreference(Long id) {
        log.info("撤回条件:ids={}",id);
        TaxPreferenceDO taxPreferenceDO=new TaxPreferenceDO();
        taxPreferenceDO.setId(id);
        taxPreferenceDO.setTaxPreferenceStatus(TaxPreferenceStatus.UNRELEASED.getValue());
        taxPreferenceMapper.updateById(taxPreferenceDO);
        log.info("id为{}的税收优惠撤回成功！",id);
        return ResultVO.ok();
    }

    /**
     * 获取申报信息
     *
     * @return submitConditionVO
     */
    private List<SubmitConditionVO> getSubmitConditionVOS(List<SubmitConditionDO> submitConditionDOS) {
        List<SubmitConditionVO> submitConditionVOList = new ArrayList<>();
        submitConditionDOS.forEach(submitConditionDO ->
                {
                    SubmitConditionVO submitConditionVO = new SubmitConditionVO();
                    BeanUtils.copyProperties(submitConditionDO, submitConditionVO);
                    submitConditionVOList.add(submitConditionVO);
                }
        );
        return submitConditionVOList;
    }

    /**
     * 获取政策法规信息
     *
     * @return taxPreferencePoliciesVO
     */
    private List<TaxPreferencePoliciesVO> getTaxPreferencePoliciesVOS(Map<String, Object> columnMap) {
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOS = taxPreferencePoliciesMapper.selectByMap(columnMap);
        List<TaxPreferencePoliciesVO> taxPreferencePoliciesVOList = new ArrayList<>();
        taxPreferencePoliciesDOS.forEach(taxPreferencePoliciesDO ->
                {
                    TaxPreferencePoliciesVO taxPreferencePoliciesVO = new TaxPreferencePoliciesVO();
                    BeanUtils.copyProperties(taxPreferencePoliciesDO, taxPreferencePoliciesVO);
                    taxPreferencePoliciesVOList.add(taxPreferencePoliciesVO);
                }
        );
        return taxPreferencePoliciesVOList;
    }

    /**
     * 修改税收优惠申报条件
     */
    private void updateSubmitConditionByTaxPreferenceId(TaxPreferenceDTO taxPreferenceDTO) {
        //采取先删除后添加的方式
        HashMap<String, Object> columnMap = new HashMap<>(16);
        columnMap.put("tax_preference_id", taxPreferenceDTO.getId());
        submitConditionMapper.deleteByMap(columnMap);
        TaxPreferenceDO taxPreferenceDO = getPreferenceDO(taxPreferenceDTO);
        insertSubmitConditionDOs(taxPreferenceDTO, taxPreferenceDO);
    }

    private TaxPreferenceDO getPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        taxPreferenceDO.setId(taxPreferenceDTO.getId());
        return taxPreferenceDO;
    }

    /**
     * 新增到申报条件表t_submit_condition
     */
    public void insertSubmitConditionDOs(TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        List<SubmitConditionDTO> submitConditionDTOList = taxPreferenceDTO.getSubmitConditionDTOList();
        if (submitConditionDTOList != null && submitConditionDTOList.size() > 0) {
            for (int i = 0; i < submitConditionDTOList.size(); i++) {
                SubmitConditionDO submitConditionDO = new SubmitConditionDO();
                BeanUtils.copyProperties(submitConditionDTOList.get(i), submitConditionDO);
                submitConditionDO.setTaxPreferenceId(taxPreferenceDO.getId());
                submitConditionDO.setSort((long) i+1);
                submitConditionMapper.insert(submitConditionDO);
            }
        }
    }

    /**
     * 填充TaxPreferencePoliciesDO对象
     */
    private List<TaxPreferencePoliciesDO> getTaxPreferencePoliciesDO(TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {

        TaxPreferencePoliciesDO preferencePoliciesDO = new TaxPreferencePoliciesDO();
        //政策法规对象集合
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOList = new ArrayList<>();
        List<TaxPreferencePoliciesDTO> taxPreferencePoliciesDTOList = taxPreferenceDTO.getTaxPreferencePoliciesDTOList();
        if (taxPreferencePoliciesDTOList != null && taxPreferencePoliciesDTOList.size() > 0) {
            for (int i = 0; i < taxPreferencePoliciesDTOList.size(); i++) {
                BeanUtils.copyProperties(taxPreferencePoliciesDTOList.get(i), preferencePoliciesDO);
                preferencePoliciesDO.setTaxPreferenceId(taxPreferenceDO.getId());
                preferencePoliciesDO.setSort((long) i+1);
                taxPreferencePoliciesDOList.add(preferencePoliciesDO);
            }
        }
        return taxPreferencePoliciesDOList;
    }

    /**
     * 填充taxPreferenceDO对象
     */
    private TaxPreferenceDO getTaxPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        BeanUtils.copyProperties(taxPreferenceDTO, taxPreferenceDO);
        taxPreferenceDO.setDeleted(false);
        taxPreferenceDO.setUpdateTime(LocalDateTime.now());
        taxPreferenceDO.setValidity(taxPreferenceDO.getValidity());
        taxPreferenceDO.setTaxPreferenceStatus(TaxPreferenceStatus.UNRELEASED.getValue());
        return taxPreferenceDO;
    }


}

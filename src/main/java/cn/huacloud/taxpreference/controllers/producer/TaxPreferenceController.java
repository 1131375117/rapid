package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

/**
 * 优惠政策接口
 *
 * @author fuhua
 */
@Api("税收优惠")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TaxPreferenceController {
    @Autowired
    private TaxPreferenceService taxPreferenceService;
    /**
     * 税收优惠事项查询接口
     * 查询关键词:优惠事项名称
     * 排序字段:发布时间,更新时间
     * 查询条件:税种种类,纳税人登记注册类型,纳税人类型,
     *        适用行业,企业类型,纳税信用等级,有效性,发布时间,
     *        税收优惠事项是否属于自我发布或待发布
     */

    @ApiOperation("税收优惠新增接口")
    @PostMapping("taxPreference")
    public ResultVO<Void> insertTaxPreference(@Validated(ValidationGroup.Create.class) @RequestBody TaxPreferenceDTO taxPreferenceDTO) {
        ResultVO<Void> resultVO = taxPreferenceService.insertTaxPreference(taxPreferenceDTO);
        return resultVO;
    }

    @ApiOperation("税收优惠修改接口")
    @PutMapping("taxPreference")
    public ResultVO<Void> updateTaxPreference(@RequestBody TaxPreferenceDTO taxPreferenceDTO) {
        ResultVO<Void> resultVO = taxPreferenceService.updateTaxPreference(taxPreferenceDTO);
        return resultVO;
    }

    /**
     * 税收优惠事项基本信息展示
     * 查询条件:优惠事项id
     */
    @ApiOperation("税收优惠详情获取")
    @GetMapping("taxPreference/{id}")
    public ResultVO<TaxPreferenceVO> queryTaxPreferenceInfo(@Validated@NotEmpty(message = "id不能为空")@PathVariable Long id) {
        ResultVO<TaxPreferenceVO> resultVO = taxPreferenceService.queryTaxPreferenceInfo(id);
        return resultVO;
    }



    /**
     * 税收优惠事项删除
     * 删除条件:优惠事项id
     */

    /**
     * 税收优惠事项批量删除
     * 删除条件:优惠事项idlist
     */
}

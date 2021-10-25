package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPreferencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPreferencesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

/**
 * 优惠政策接口
 *
 * @author fuhua
 */
@Api(tags = "税收优惠")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TaxPreferenceController {

    private final TaxPreferenceService taxPreferenceService;

    @ApiOperation("税收优惠查询接口")
    @PostMapping("queryTaxPreference")
    public ResultVO<PageVO<QueryTaxPreferencesVO>> queryTaxPreference(@RequestBody QueryTaxPreferencesDTO queryTaxPreferencesDTO) {
        return taxPreferenceService.queryTaxPreferenceList(queryTaxPreferencesDTO);
    }

    @ApiOperation("税收优惠新增接口")
    @PostMapping("taxPreference")
    public ResultVO<Void> insertTaxPreference(@Validated(ValidationGroup.Create.class) @RequestBody TaxPreferenceDTO taxPreferenceDTO) {
         taxPreferenceDTO.setInputUserId(UserUtil.getCurrentUser().getId());
        return taxPreferenceService.insertTaxPreference(taxPreferenceDTO);
    }

    @ApiOperation("税收优惠修改接口")
    @PutMapping("taxPreference")
    public ResultVO<Void> updateTaxPreference(@Validated(ValidationGroup.Update.class)@RequestBody TaxPreferenceDTO taxPreferenceDTO) {
        return taxPreferenceService.updateTaxPreference(taxPreferenceDTO);
    }

    /**
     * 税收优惠事项基本信息展示
     * 查询条件:优惠事项id
     */
    @ApiOperation("税收优惠详情获取")
    @GetMapping("taxPreference/{id}")
    public ResultVO<TaxPreferenceVO> queryTaxPreferenceInfo(@Validated @NotEmpty(message = "id不能为空") @PathVariable Long id) {
        return taxPreferenceService.queryTaxPreferenceInfo(id);
    }


    /**
     * 税收优惠事项批量删除
     * 删除条件:优惠事项idlist
     */
}

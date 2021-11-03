package cn.huacloud.taxpreference.controllers.producer;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryAbolishDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPreferencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPreferencesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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

    @PermissionInfo(name = "税收优惠查询接口", group = PermissionGroup.TAX_PREFERENCE)
    @SaCheckPermission("producer_taxPreference_query")
    @ApiOperation("税收优惠查询接口")
    @PostMapping("queryTaxPreference")
    public ResultVO<PageVO<QueryTaxPreferencesVO>> queryTaxPreference(@RequestBody QueryTaxPreferencesDTO queryTaxPreferencesDTO) {
        queryTaxPreferencesDTO.paramReasonable();
        return taxPreferenceService.queryTaxPreferenceList(queryTaxPreferencesDTO, UserUtil.getCurrentUser().getId());
    }

    @PermissionInfo(name = "税收优惠新增接口", group = PermissionGroup.TAX_PREFERENCE)
    @SaCheckPermission("producer_taxPreference_insert")
    @ApiOperation("税收优惠新增接口")
    @PostMapping("taxPreference")
    public ResultVO<Void> insertTaxPreference(@Validated(ValidationGroup.Create.class) @RequestBody TaxPreferenceDTO taxPreferenceDTO) {
        taxPreferenceDTO.setInputUserId(UserUtil.getCurrentUser().getId());
        return taxPreferenceService.insertTaxPreference(taxPreferenceDTO);
    }

    @PermissionInfo(name = "税收优惠修改接口", group = PermissionGroup.TAX_PREFERENCE)
    @SaCheckPermission("producer_taxPreference_update")
    @ApiOperation("税收优惠修改接口")
    @PutMapping("taxPreference")
    public ResultVO<Void> updateTaxPreference(@Validated(ValidationGroup.Update.class) @RequestBody TaxPreferenceDTO taxPreferenceDTO) {
        return taxPreferenceService.updateTaxPreference(taxPreferenceDTO);
    }

    /**
     * 税收优惠事项基本信息展示
     * 查询条件:优惠事项id
     */
    @PermissionInfo(name = "税收优惠详情接口", group = PermissionGroup.TAX_PREFERENCE)
    @SaCheckPermission("producer_taxPreference_detail")
    @ApiOperation("税收优惠详情获取")
    @GetMapping("taxPreference/{id}")
    public ResultVO<TaxPreferenceVO> queryTaxPreferenceInfo(@Validated @NotEmpty(message = "id不能为空") @PathVariable Long id) {
        return taxPreferenceService.queryTaxPreferenceInfo(id);
    }


    /**
     * 税收优惠事项批量删除
     * 删除条件:优惠事项idlist
     */
    @PermissionInfo(name = "税收优惠事项删除", group = PermissionGroup.TAX_PREFERENCE)
    @SaCheckPermission("producer_taxPreference_delete")
    @ApiOperation("税收优惠事项删除")
    @DeleteMapping("taxPreference/{id}")
    public ResultVO<Void> taxPreference(@Validated @NotEmpty(message = "id不能为空") @PathVariable Long[] id) {
        return taxPreferenceService.deleteTaxPreference(id);
    }

    /**
     * 税收优惠事项内容撤回
     * 删除条件:优惠事项idlist
     */
    @PermissionInfo(name = "税收优惠事项内容撤回", group = PermissionGroup.TAX_PREFERENCE)
    @SaCheckPermission("producer_taxPreference_revoke")
    @ApiOperation("税收优惠事项内容撤回")
    @PutMapping("reTaxPreference/{id}")
    public ResultVO<Void> taxPreference(@Validated @NotEmpty(message = "id不能为空") @PathVariable Long id) {
        return taxPreferenceService.reTaxPreference(id);
    }

}

package cn.huacloud.taxpreference.controllers.producer;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 优惠政策接口
 *
 * @author fuhua
 */
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

    /**
     * 税收优惠事项新增或修改
     * 新增条件:
     * 1、新增事项码值类型:收入税种种类,纳税人登记注册类型,纳税人类型,
     * 适用行业,企业类型,纳税信用等级
     * 2、政策信息:优惠事项名称,优惠政策（文件名/文号),有效期间,具体优惠内容摘要
     * 3、申报条件:自定义字段
     * 4、资料要求:留存备查资料,提交税务机关资料,资料报送时限,申报表填写简要说明
     * 5、标签集合
     * <p>
     * 修改条件:税收优惠事项id
     */
    @ApiOperation("税收优惠新增接口")
    @PostMapping("insertTaxPreference")
    public ResultVO<Void> insertTaxPreference(@RequestBody TaxPreferenceDTO taxPreferenceDTO) {
        ResultVO<Void> resultVO = taxPreferenceService.insertTaxPreference(taxPreferenceDTO);
        return resultVO;
    }
    @ApiOperation("税收优惠修改接口")
    @PutMapping("insertTaxPreference")
    public ResultVO<Void> updateTaxPreference(@RequestBody TaxPreferenceDTO taxPreferenceDTO) {
        ResultVO<Void> resultVO = taxPreferenceService.updateTaxPreference(taxPreferenceDTO);
        return resultVO;
    }


    /**
     * 税收优惠事项基本信息展示
     * 查询条件:优惠事项id
     */

    /**
     * 税收优惠事项发布申请
     * 发布申请条件:优惠事项id
     */

    /**
     * 税收优惠事项批量发布申请
     * 发布申请条件:优惠事项idlist
     */

    /**
     * 税收优惠事项删除
     * 删除条件:优惠事项id
     */

    /**
     * 税收优惠事项批量删除
     * 删除条件:优惠事项idlist
     */
}

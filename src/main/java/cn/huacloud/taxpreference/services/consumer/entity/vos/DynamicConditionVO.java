package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.entity.vos.GroupVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 动态条件视图对象
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class DynamicConditionVO {
    @ApiModelProperty("企业类型")
    private List<String> enterpriseTypes;
    @ApiModelProperty("减免事项")
    private List<String> taxPreferenceItems;
    @ApiModelProperty("动态条件")
    private List<GroupVO<GroupVO<String>>> conditions;
}

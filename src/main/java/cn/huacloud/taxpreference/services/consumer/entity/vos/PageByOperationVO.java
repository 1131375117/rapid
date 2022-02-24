package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class PageByOperationVO extends PageVO {
    @ApiModelProperty("操作记录返回list")
    private List<OperationPageVO> list;
}

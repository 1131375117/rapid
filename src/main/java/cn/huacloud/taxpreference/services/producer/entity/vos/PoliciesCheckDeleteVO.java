package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 校验删除的对象
 * @author wuxin
 */
@Data
@Accessors(chain = true)
@ApiModel
public class PoliciesCheckDeleteVO {

    @ApiModelProperty("校验删除的状态")
    private CheckStatus checkStatus;

    @ApiModelProperty("校验删除后返回的数据")
    List<TaxPreferenceCountVO> checkList;

    public enum CheckStatus{
        OK,
        INFO,
        CAN_NOT;
    }

}

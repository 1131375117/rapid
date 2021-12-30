package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.utils.DocCodeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 文号视图对象
 * @author wangkh
 */
@Getter
@Setter
public class DocCodeVO {
    @ApiModelProperty("字号")
    private String docWordCode;
    @ApiModelProperty("年号")
    private Integer docYearCode;
    @ApiModelProperty("序号")
    private Integer docNumCode;

    @Override
    public String toString() {
        return DocCodeUtil.getDocCode(this);
    }
}

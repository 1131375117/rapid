package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author fuhua
 **/
@Data
public class OrganizationVO {
    @ApiModelProperty("名称")
    private String title;
    @ApiModelProperty("值")
    private String value;
    //机构类型
    @ApiModelProperty("机构类型")
    private String organizationType;
    @ApiModelProperty("子节点")
    private List<OrganizationVO> children;
}

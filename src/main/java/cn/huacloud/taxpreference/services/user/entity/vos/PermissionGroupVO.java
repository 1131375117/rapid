package cn.huacloud.taxpreference.services.user.entity.vos;

import cn.huacloud.taxpreference.common.entity.vos.TreeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 权限分组视图
 * @author wangkh
 */
@ApiModel("权限分组视图")
@Data
public class PermissionGroupVO {

    private String groupName;

    private String groupCode;

    private List<PermissionVO> permissions;
}

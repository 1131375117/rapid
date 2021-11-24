package cn.huacloud.taxpreference.services.common.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据统计视图
 * @author wangkh
 */
@Accessors(chain = true)
@Getter
@Setter
public class SysCodeCountVO extends SysCodeSimpleVO {
    @ApiModelProperty("总记录数")
    private Long total;

    /**
     * 根据系统码值简单视图获取数据统计视图
     * @param sysCodeSimpleVO 系统码值简单视图
     * @return 数据统计视图
     */
    public static SysCodeCountVO of(SysCodeSimpleVO sysCodeSimpleVO) {
        SysCodeCountVO sysCodeCountVO = new SysCodeCountVO();
        sysCodeCountVO.setCodeName(sysCodeSimpleVO.getCodeName());
        sysCodeCountVO.setCodeValue(sysCodeSimpleVO.getCodeValue());
        return sysCodeCountVO;
    }
}

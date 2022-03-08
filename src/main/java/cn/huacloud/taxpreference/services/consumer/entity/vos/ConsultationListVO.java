package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhengkai.blog.csdn.net
 * @description 热门咨询contentVO
 * @date 2022-02-17
 */
@Data
public class ConsultationListVO {

    private Long id;
    @ApiModelProperty("开始时间")
    private LocalDateTime createTime;
    @ApiModelProperty("税种")
    private List<SysCodeSimpleVO> taxCategories;
    @ApiModelProperty("内容")
    private String consultationContent;


}
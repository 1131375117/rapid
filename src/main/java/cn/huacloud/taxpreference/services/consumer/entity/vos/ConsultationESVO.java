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
public class ConsultationESVO extends UserSubScribeInfo {

    private Long id;
    @ApiModelProperty("浏览量")
    private Long views;
    @ApiModelProperty("收藏量")
    private Long collections;
    @ApiModelProperty("回答时间")
    private LocalDateTime finishTime;
    @ApiModelProperty("是否公开")
    private Long published;
    @ApiModelProperty("回复状态,HAVE_REPLY-已答复，NOT_REPLY未答复")
    private String status;
    @ApiModelProperty("行业")
    private List<SysCodeSimpleVO> industries;
    @ApiModelProperty("咨询者id")
    private Long customerUserId;
    @ApiModelProperty("追问次数,只在详情接口使用")
    private Integer appendCount;

    private List<ConsultationContentESVO> consultationContent;


}
package cn.huacloud.taxpreference.services.common.entity.dos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * t_user_monitor_info
 * @author 付华
 */
@ApiModel(value="cn.huacloud.taxpreference.services.common.entity.dos.UserMonitorInfoDO调用记录表")
@Data
@Accessors(chain = true)
public class UserMonitorInfoDO implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value="主键id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long akId;

    /**
     * 请求方式
     */
    @ApiModelProperty(value="请求方式")
    private String requestMethod;

    /**
     * 接口名
     */
    @ApiModelProperty(value="接口名")
    private String path;

    /**
     * 请求参数
     */
    @ApiModelProperty(value="请求参数")
    private String requestParams;

    /**
     * 请求开始时间
     */
    @ApiModelProperty(value="请求开始时间")
    private LocalDateTime startTime;

    /**
     * 请求结束时间
     */
    @ApiModelProperty(value="请求结束时间")
    private LocalDateTime endTime;

    /**
     * 本次接口调用时长
     */
    @ApiModelProperty(value="本次接口调用时长")
    private Long invokeTime;

    /**
     * 本次调用成功失败
     */
    @ApiModelProperty(value="本次调用成功失败")
    private String invokeStatus;

    /**
     * 调用信息
     */
    @ApiModelProperty(value="调用信息")
    private String invokeMsg;

    private static final long serialVersionUID = 1L;
}
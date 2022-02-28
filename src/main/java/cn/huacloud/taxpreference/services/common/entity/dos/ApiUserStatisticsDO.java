package cn.huacloud.taxpreference.services.common.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * t_api_user_statistics
 * @author 付华
 */
@ApiModel(value="cn.huacloud.taxpreference.services.common.entity.dos.ApiUserStatisticsDOOpenAPI统计信息表")
@Data
@Accessors(chain = true)
@TableName("t_api_user_statistics")
public class ApiUserStatisticsDO implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value="主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private String akId;

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
     * 请求耗时最长时间
     */
    @ApiModelProperty(value="请求耗时最长时间")
    private Long maxTime;

    /**
     * 请求耗时最短时间
     */
    @ApiModelProperty(value="请求耗时最短时间")
    private Long minTime;

    /**
     * 接口调用总时长
     */
    @ApiModelProperty(value="接口调用总时长")
    private Long totalTime;

    /**
     * 用户接口总共调用次数
     */
    @ApiModelProperty(value="用户接口总共调用次数")
    private Long totalRequestCount;

    /**
     * 用户成功次数
     */
    @ApiModelProperty(value="用户成功次数")
    private Long successCount;

    /**
     * 请求失败次数
     */
    @ApiModelProperty(value="请求失败次数")
    private Long errorCount;

    private static final long serialVersionUID = 1L;
}
package cn.huacloud.taxpreference.services.producer.entity.vos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策解读详情VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesExplainDetailVO {

	/**
	 * 主键Id
	 */
	@ApiModelProperty("主键Id")
	private Long id;

	/**
	 * 政策Id
	 */
	@ApiModelProperty("政策解读中政策Id")
	private Long policiesId;

	/**
	 * 标题
	 */
	@ApiModelProperty("标题")
	private String title;

	/**
	 * 来源
	 */
	@ApiModelProperty("来源")
	private String docSource;

	/**
	 * 发布日期
	 */
	@ApiModelProperty("发布日期")
	private LocalDate releaseDate;

	/**
	 * 正文
	 */
	@ApiModelProperty("正文")
	private String content;

	/**
	 * 录入人用户Id
	 */
	@ApiModelProperty("录入人用户Id")
	private Long inputUserId;

	@ApiModelProperty("热门问答中政策id")
	private String policiesIds;

}

package cn.huacloud.taxpreference.services.producer.entity.vos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 政策解读详情VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesExplainDetailVO extends PoliciesCommonListVO{

	/**
	 * 主键Id
	 */
	@ApiModelProperty("政策解读id")
	private Long id;

	/**
	 * 政策Id
	 */
	@ApiModelProperty("政策解读中政策Id")
	private Long policiesId;


	/**
	 * 正文
	 */
	@ApiModelProperty("正文")
	private String content;

	/**
	 * 录入人用户Id
	 */
	@ApiModelProperty(hidden = true,value = "录入人用户Id")
	private Long inputUserId;

	@ApiModelProperty("政策法规对象")
	private PoliciesTitleVO policies;

	@ApiModelProperty("数据来源网站链接")
	private String spiderUrl;
}

package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.services.producer.entity.enums.FrequentlyAskedQuestionStatusEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 热点问答VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class FrequentlyAskedQuestionVO extends PoliciesCommonListVO{
	/**
	 * 主键ID
	 */
	@ApiModelProperty("id")
	private Long id;

	/**
	 * 政策ID集合
	 */
	@ApiModelProperty("政策ID集合")
	private List<String> policiesIds;

	/**
	 * 回答
	 */
	@ApiModelProperty("回答")
	private String content;


	/**
	 * 热门问答状态
	 */
	@ApiModelProperty("热门问答状态")
	private FrequentlyAskedQuestionStatusEnum frequentlyAskedQuestionStatus;


}

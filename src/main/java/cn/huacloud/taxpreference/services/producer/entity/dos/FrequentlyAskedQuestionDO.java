package cn.huacloud.taxpreference.services.producer.entity.dos;

import cn.huacloud.taxpreference.services.producer.entity.enums.FrequentlyAskedQuestionStatusEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 热点问答实体
 *
 * @author wuxin
 */
@Data
@TableName("t_frequently_asked_question")
@Accessors(chain = true)
public class FrequentlyAskedQuestionDO {
	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 政策ID集合
	 */
	private String policiesIds;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 回答
	 */
	private String content;

	/**
	 * 来源
	 */
	private String docSource;

	/**
	 * 发布日期
	 */
	private LocalDate releaseDate;

	/**
	 * 热门问答状态
	 */
	private FrequentlyAskedQuestionStatusEnum frequentlyAskedQuestionStatus;

	/**
	 * 解答机构
	 */
	private String answerOrganization;

	private String organizationType;
	/**
	 * 录入人用户id
	 */
	private Long inputUserId;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
	private LocalDateTime updateTime;

	/**
	 * 逻辑删除
	 */
	private Boolean deleted;
	/**
	 * 主题分类
	 * */
	private String subjectType;

}

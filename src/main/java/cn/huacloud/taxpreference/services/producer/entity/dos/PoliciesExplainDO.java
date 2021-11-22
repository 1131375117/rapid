package cn.huacloud.taxpreference.services.producer.entity.dos;

import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesExplainStatusEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 政策解读实体
 *
 * @author wuxin
 */
@Data
@TableName("t_policies_explain")
public class PoliciesExplainDO {

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 政策Id
	 */
	private Long policiesId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 来源
	 */
	private String docSource;

	/**
	 * 发布日期
	 */
	private LocalDate releaseDate;

	/**
	 * 正文
	 */
	private String content;

	/**
	 * 录入人用户ID
	 */
	private Long inputUserId;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 逻辑删除
	 */

	private Boolean deleted;

	/**
	 * 政策解读状态
	 */
	private PoliciesExplainStatusEnum policiesExplainStatus;
}

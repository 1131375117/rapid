package cn.huacloud.taxpreference.services.producer.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description: 申报条件表实体
 * @author: fuhua
 * @create: 2021-10-21 10:08
 **/
@Data
@TableName("t_submit_condition")
public class SubmitConditionDO {

	/**
	 * 主键自增
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 税收优惠ID
	 */
	private Long taxPreferenceId;

	/**
	 * 条件名称
	 */
	private String conditionName;

	/**
	 * 具体要求
	 */
	private String requirement;

	/**
	 * 排序字段
	 */
	private Long sort;

}

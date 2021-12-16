package cn.huacloud.taxpreference.services.producer.entity.dos;


import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策法规实体
 *
 * @author wuxin
 */
@Data
@TableName("t_policies")
@Accessors(chain = true)
public class PoliciesDO {

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 文号
	 */
	private String docCode;
	/**
	 * 字号
	 */
	private String docWordCode;

	/**
	 * 年号
	 */
	private Integer docYearCode;

	/**
	 * 编号
	 */
	private Integer docNumCode;

	/**
	 * 所属区域名称
	 */
	private String areaName;

	/**
	 * 所属区域码值
	 */
	private String areaCode;

	/**
	 * 来源
	 */
	private String docSource;

	/**
	 * 所属税种名称
	 */
	private String taxCategoriesNames;

	/**
	 * 所属税种码值
	 */
	private String taxCategoriesCodes;

	/**
	 * 纳税人资格认定类型名称
	 */
	private String taxpayerIdentifyTypeNames;

	/**
	 * 纳税人资格认定类型码值
	 */
	private String taxpayerIdentifyTypeCodes;

	/**
	 * 适用企业类型名称
	 */
	private String enterpriseTypeNames;

	/**
	 * 适用企业类型码值
	 */
	private String enterpriseTypeCodes;

	/**
	 * 适用行业名称
	 */
	private String industryNames;

	/**
	 * 适用行业码值
	 */
	private String industryCodes;

	/**
	 * 有效性
	 */
	private ValidityEnum validity;

	/**
	 * 发布日期
	 */
	private LocalDate releaseDate;

	/**
	 * 所属专题
	 */
	private String specialSubject;

	/**
	 * 摘要
	 */
	private String digest;

	/**
	 * 正文
	 */
	private String content;

	/**
	 * 标签集合
	 */
	private String labels;

	/**
	 * 政策法规状态
	 */
	private PoliciesStatusEnum policiesStatus;

	/**
	 * 废止说明
	 */
	private String abolishNote;

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
}

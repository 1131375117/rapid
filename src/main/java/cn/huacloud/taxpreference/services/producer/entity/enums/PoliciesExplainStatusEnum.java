package cn.huacloud.taxpreference.services.producer.entity.enums;


import com.baomidou.mybatisplus.annotation.IEnum;


/**
 * 政策解读状态
 *
 * @author wuxin
 */
public enum PoliciesExplainStatusEnum implements IEnum<String> {

	/**
	 * REPTILE_SYNCHRONIZATION 爬虫同步
	 * PUBLISHED 已发布
	 */
	REPTILE_SYNCHRONIZATION("爬虫同步"),
	PUBLISHED("已发布");

	private final String name;

	PoliciesExplainStatusEnum(String name) {
		this.name = name;
	}

	@Override
	public String getValue() {
		return this.name();
	}
}

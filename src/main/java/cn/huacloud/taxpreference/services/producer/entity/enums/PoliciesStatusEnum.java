package cn.huacloud.taxpreference.services.producer.entity.enums;


import com.baomidou.mybatisplus.annotation.IEnum;


/**
 * 政策法规废止状态
 *
 * @author wuxin
 */
public enum PoliciesStatusEnum implements IEnum<String> {

	/**
	 * REPTILE_SYNCHRONIZATION 爬虫同步
	 * PUBLISHED 已发布
	 */
	REPTILE_SYNCHRONIZATION("爬虫同步"),
	PUBLISHED("已发布");

	private final String name;

	PoliciesStatusEnum(String name) {
		this.name = name;
	}

	@Override
	public String getValue() {
		return this.name();
	}
}

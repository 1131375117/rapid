package cn.huacloud.taxpreference.services.producer.entity.enums;


import cn.huacloud.taxpreference.common.enums.SysCodeGetter;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * 有效性状态
 *
 * @author wuxin
 */
public enum ValidityEnum implements IEnum<String>, SysCodeGetter {

	/**
	 * FULL_TEXT_VALID 全文有效
	 * FULL_TEXT_REPEAL 全文废止
	 * PARTIAL_REPEAL 部分废止
	 */


	FULL_TEXT_VALID("全文有效"),
	INVALID("失效"),
	FULL_TEXT_REPEAL("全文废止"),
	PARTIAL_VALID("部分有效"),
	CLAUSE_INVALIDITY("条款失效"),
	FULL_TEXT_INVALIDATION("全文失效"),
	PARTIAL_REPEAL("部分废止"),
	CLAUSE_REPEAL("条款废止"),
	UNKNOWN("未知");

	@Getter
	private final String name;

	ValidityEnum(String name) {
		this.name = name;
	}

	@Override
	public String getValue() {
		return this.name();
	}

	public static HashMap<String, String> ValidMap() {
		HashMap<String, String> validMap = new HashMap<>();
		for (ValidityEnum validityEnum : EnumSet.allOf(ValidityEnum.class)) {
			validMap.put(validityEnum.name, validityEnum.getValue());
		}
		return validMap;
	}

	@Override
	public SysCodeSimpleVO getSysCode() {
		if (this == UNKNOWN) {
			return new SysCodeSimpleVO();
		}
		return new SysCodeSimpleVO().setCodeName(getName())
				.setCodeValue(name());
	}

	/**
	 * 通过名称匹配获取枚举
	 * @param name 名称
	 * @return 枚举
	 */
	public static ValidityEnum getByName(String name) {
		for (ValidityEnum value : values()) {
			if (value.getName().equals(name)) {
				return value;
			}
		}
		return ValidityEnum.UNKNOWN;
	}
}

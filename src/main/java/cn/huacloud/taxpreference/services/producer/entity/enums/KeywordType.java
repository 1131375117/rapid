package cn.huacloud.taxpreference.services.producer.entity.enums;


import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 查询关键字
 *
 * @author wuxin
 */
public enum KeywordType implements IEnum<String> {
    TITLE,
    DOC_CODE;

    @Override
    public String getValue() {
        return this.name();
    }
}

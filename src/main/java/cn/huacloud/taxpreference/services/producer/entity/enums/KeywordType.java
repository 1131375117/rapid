package cn.huacloud.taxpreference.services.producer.entity.enums;


import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 查询关键字
 *
 * @author wuxin
 */

public enum KeywordType implements IEnum<String> {
    //政策法规标题
    TITLE,
    //政策法规文号
    DOC_CODE;

    @Override
    public String getValue() {
        return this.name();
    }
}

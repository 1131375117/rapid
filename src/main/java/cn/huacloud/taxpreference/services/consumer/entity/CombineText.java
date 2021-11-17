package cn.huacloud.taxpreference.services.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 待组合文本
 * @author wangkh
 */
@Getter
@Setter
@AllArgsConstructor
public class CombineText {
    /**
     * 是否为html富文本
     */
    private Boolean html;
    /**
     * 文本内容
     */
    private String text;

    /**
     * 构建纯文本
     */
    public static CombineText ofText(String text) {
        return new CombineText(false, text);
    }

    /**
     * 构建Html
     */
    public static CombineText ofHtml(String text) {
        return new CombineText(true, text);
    }
}

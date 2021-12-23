package cn.huacloud.taxpreference.common.entity.dtos;

import cn.huacloud.taxpreference.common.enums.consumer.SearchScope;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 扩展搜索查询
 * @author wangkh
 */
@Getter
@Setter
public class ExSearchQueryDTO extends KeywordPageQueryDTO {

    @ApiModelProperty("查询范围")
    private SearchScope searchScope;
    @ApiModelProperty("是否精确查询")
    private Boolean preciseQuery;
    /**
     * 关键字使用空隔开
     */
    @ApiModelProperty(hidden = true)
    private List<String> keywordSplit;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (searchScope == null) {
            searchScope = SearchScope.TITLE;
        }
        if (preciseQuery == null) {
            preciseQuery = true;
        }
        keywordSplit = splitByBlank(getKeyword());
    }

    /**
     * 空格字符
     * ' ' 8197
     * ' ' 8195
     * '　' 12288
     * ' ' 32
     * ' ' 160
     */
    private static final Set<Character> BLANK_SET = Sets.newHashSet((char) 8197, (char) 8195, (char) 12288, (char) 32, (char) 160);

    /**
     * 使用空格分割字符串
     * @param target 模板字符串
     * @return list
     */
    private List<String> splitByBlank(String target) {
        if (StringUtils.isBlank(target)) {
            return null;
        }
        char[] chars = target.toCharArray();
        StringBuilder sb = new StringBuilder();
        List<String> keywordSplit = new ArrayList<>();
        for (char ch : chars) {
            if (BLANK_SET.contains(ch)) {
                if (sb.length() > 0) {
                    keywordSplit.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else {
                sb.append(ch);
            }
        }
        if (sb.length() > 0) {
            keywordSplit.add(sb.toString());
        }
        if (keywordSplit.isEmpty()) {
            return null;
        }
        return keywordSplit;
    }
}

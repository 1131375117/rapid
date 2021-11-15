package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.RangeField;
import cn.huacloud.taxpreference.common.entity.dtos.LocalDateRangeQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wangkh
 */
@Getter
@Setter
public class FAQSearchQueryDTO extends AbstractHighlightPageQueryDTO {

    @Override
    public List<String> indexList() {
        return Collections.singletonList("frequently_asked_question");
    }

    @Override
    public List<String> searchFields() {
        return Arrays.asList("title", "content");
    }

}

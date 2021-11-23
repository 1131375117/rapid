package cn.huacloud.taxpreference.common.entity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * 关键字分页查询对象
 *
 * @author wangkh
 */
@Getter
@Setter
@ApiModel
public class KeywordPageQueryDTO extends PageQueryDTO {

    @ApiModelProperty("查询关键字")
    private String keyword;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (StringUtils.isBlank(keyword)) {
            keyword = null;
        }
    }

    /**
     * 把空字符串参数设置为null或者进行trim
     */
    protected void stringParamNullOrTrim() {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                if (!pd.getPropertyType().isAssignableFrom(String.class)) {
                    continue;
                }
                String value = (String) pd.getReadMethod().invoke(this);
                if (StringUtils.isBlank(value)) {
                    value = null;
                } else {
                    value = value.trim();
                }
                pd.getWriteMethod().invoke(this, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fuhua
 **/
@Data
public class MyConsultationDTO extends ConsultationQueryDTO {

    /**
     * 查询我的咨询
     */
    @ApiModelProperty(value = "查询我的咨询",hidden = true)
    @FilterField("customerUserId")
    private Long customerUserId;

    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getConsultation().getAlias();
    }
}

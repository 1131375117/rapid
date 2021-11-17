package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @description: 税收优惠政策法规关联表DTO
 * @author: fuhua
 * @create: 2021-10-21 09:59
 **/
@Data
public class TaxPreferencePoliciesDTO {



    /**
     * 政策法规ID-政策法规
     */
    @ApiModelProperty("政策法规ID")
    @NotNull(message = "政策法规ID不能为空",groups = {ValidationGroup.Update.class,ValidationGroup.Create.class})
    private Long policiesId;

    /**
     * 有效期起-政策法规
     */
    @ApiModelProperty("有效期起")
    @NotNull(message = "有效期不能为空",groups = {ValidationGroup.Update.class,ValidationGroup.Create.class})
    private LocalDate validityBeginDate;

    /**
     * 有效期至-政策法规
     */
    @ApiModelProperty("有效期至")
    //@NotNull(message = "有效期不能为空",groups = {ValidationGroup.Update.class,ValidationGroup.Create.class})
    private LocalDate validityEndDate;

    /**
     * 具体优惠内容摘要
     */
    @ApiModelProperty("具体优惠内容摘要")
    @NotEmpty(message = "具体优惠内容摘要不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String digest;



}

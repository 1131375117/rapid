package cn.huacloud.taxpreference.services.consumer.entity.vos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 政策法规摘要视图，该对象为税收优惠的子对象
 * @author wangkh
 */
@Data
public class PoliciesDigestSerachVO {
    /**
     * 标题
     */
    private String title;
    /**
     * 文号
     */
    private String docCode;
    /**
     * 有效期起
     */
    private LocalDate validityBeginDate;
    /**
     * 有效期至
     */
    private LocalDate validityEndDate;
    /**
     * 税收优惠摘要
     */
    private String digest;
}

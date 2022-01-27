package cn.huacloud.taxpreference.services.producer.entity.vos;

import lombok.Data;

import java.util.List;

/**
 * @author fuhua
 **/
@Data
public class OrganizationVO {
    private String title;
    private String value;
    //机构类型
    private String organizationType;
    private List<OrganizationVO> children;
}

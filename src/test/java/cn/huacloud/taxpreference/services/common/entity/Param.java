package cn.huacloud.taxpreference.services.common.entity;

import cn.huacloud.taxpreference.common.enums.DocType;
import lombok.Data;

@Data
public class Param {
    private DocType taxPreference;
}
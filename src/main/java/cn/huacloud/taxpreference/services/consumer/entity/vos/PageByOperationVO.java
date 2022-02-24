package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class PageByOperationVO<T> extends PageVO<OperationPageVO<T>> {

}

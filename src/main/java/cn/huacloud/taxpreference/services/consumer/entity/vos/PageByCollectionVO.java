package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class PageByCollectionVO extends PageVO {
    private List<CollectionPageVO> list;
}

package cn.huacloud.taxpreference.common.entity.vos;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分页对象
 * @author wangkh
 */
@Data
@ApiModel
public class PageVO<T> {

    @ApiModelProperty("当前页")
    private Integer pageNum;
    @ApiModelProperty("每页显示条数")
    private Integer pageSize;
    @ApiModelProperty("满足条件总行数")
    private Long total;
    @ApiModelProperty("数据记录")
    private List<T> records;

    /**
     * 根据IPage创建PageVO
     * @param iPage iPage
     * @param <T> 数据记录类型
     * @return pageVO
     */
    public static <T> PageVO<T> createPageVO(IPage<?> iPage, List<T> records) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setPageNum((int) iPage.getCurrent());
        pageVO.setPageSize((int) iPage.getSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setRecords(records);
        return pageVO;
    }

    /**
     * 根据IPage创建PageVO
     * @param iPage iPage
     * @param <T> 数据记录类型
     * @return pageVO
     */
    public static <T> PageVO<T> createPageVO(IPage<T> iPage) {
        return createPageVO(iPage, iPage.getRecords());
    }
}

package cn.huacloud.taxpreference.common.entity.dtos;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询对象
 * @author wangkh
 */
@Data
@ApiModel
public class PageQueryDTO {

    @ApiModelProperty("当前页")
    private Integer pageNum;
    @ApiModelProperty("每页显示条数")
    private Integer pageSize;

    /**
     * 参数合理化
     */
    public void paramReasonable() {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
    }

    /**
     * 创建 mybatis plus 分页查询对象
     * @return 分页查询对象
     */
    public <T> IPage<T> createQueryPage() {
        return new Page<>(pageNum, pageSize);
    }
}

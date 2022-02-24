package cn.huacloud.taxpreference.common.entity.dtos;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询对象
 * @author wangkh
 */
@Getter
@Setter
@ApiModel
public class PageQueryDTO {

    @ApiModelProperty(value = "当前页", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "每页显示条数",example = "10")
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

    /**
     * 获取分页偏移量
     * @return 分页偏移量
     */
    public Integer from() {
        int from = (pageNum - 1) * pageSize;
        return Math.max(from, 0);
    }
}

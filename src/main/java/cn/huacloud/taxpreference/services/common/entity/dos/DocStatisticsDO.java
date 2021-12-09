package cn.huacloud.taxpreference.services.common.entity.dos;

import cn.huacloud.taxpreference.common.enums.DocType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统计实体
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
@TableName("t_doc_statistics")
public class DocStatisticsDO {
    /**
     * 主键id
     * */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文档类型
     * */
    private DocType docType;

    /**
     * 文档id
     * */
    private Long docId;

    /**
     * 浏览量
     * */
    private Long views;

    /**
     * 收藏数
     * */
    private Long collections;
}

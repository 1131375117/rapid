package cn.huacloud.taxpreference.services.common.entity.vos;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class AttachmentVO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 附件类型
     */
    private AttachmentType attachmentType;
    /**
     * 所属文档ID
     */
    private Long docId;
    /**
     * 附件名称
     */
    private String attachmentName;
    /**
     * 附件访问路径
     */
    private String url;
    /**
     * 排序字段
     */
    private Long sort;
    /**
     * 创建时间
     */
    private LocalDate createTime;
}

package cn.huacloud.taxpreference.services.common.entity.dos;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
@TableName("t_attachment")
public class AttachmentDO {
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
     * 文件扩展名
     */
    private String extension;
    /**
     * 附件路径
     */
    private String path;
    /**
     * 排序字段
     */
    private Long sort;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

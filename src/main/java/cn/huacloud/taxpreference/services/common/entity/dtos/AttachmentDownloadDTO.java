package cn.huacloud.taxpreference.services.common.entity.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class AttachmentDownloadDTO {
    /**
     * 附件名称
     */
    private String attachmentName;
    /**
     * 文件流
     */
    private InputStream inputStream;
}

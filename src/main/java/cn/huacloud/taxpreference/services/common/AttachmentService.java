package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.services.common.entity.vos.AttachmentVO;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 附件服务
 * @author wangkh
 */
public interface AttachmentService {

    /**
     * 附件上传接口
     *
     * @param attachmentType 附件类型
     * @param attachmentName 文件名称
     * @param extension 文件扩展名
     * @param inputStream 输入流
     * @param size 文件字节长度
     * @return 附件视图对象
     */
    AttachmentVO uploadAttachment(AttachmentType attachmentType, String attachmentName, String extension, InputStream inputStream, long size) throws Exception;

    /**
     * 把指ID的附件归属到指定文档下
     * @param docId 文档ID集合
     * @param attachmentType 附件类型
     * @param content 富文本文本内容
     * @return 文档视图对象
     */
    List<AttachmentVO> setAttachmentDocId(Long docId, AttachmentType attachmentType, String content);

    /**
     * 获取附件视图集合
     * @param attachmentType 附件类型
     * @param docId 所属文档ID
     * @return 附件视图集合
     */
    List<AttachmentVO> getAttachmentVOList(AttachmentType attachmentType, Long docId);

    /**
     * 获取附件访问URL
     * @param path
     * @return
     */
    default String getUrl(String path) {
        return "/api/v1/attachment/download/" + path;
    }

    /**
     * 下载附件
     * @param path 附件路径
     * @return 文件流
     */
    InputStream downloadAttachment(String path);
}

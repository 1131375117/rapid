package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.config.MinioConfig;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.services.common.entity.vos.AttachmentVO;
import cn.huacloud.taxpreference.services.common.mapper.AttachmentMapper;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final MinioConfig minioConfig;

    private final MinioClient minioClient;

    private final AttachmentMapper attachmentMapper;

    @Transactional
    @Override
    public AttachmentVO uploadAttachment(AttachmentType attachmentType, String attachmentName, String extension, InputStream inputStream, long size) throws Exception {

        // 计算MD5值, 如果计算效率过低，可以考虑使用UUID代替
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String md5 = SecureUtil.md5(new ByteArrayInputStream(bytes));
        // 获取上传路径
        String path = attachmentType.getPath(md5, attachmentName, extension);

        // 上传文件至minio
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioConfig.getBucket())
                .object(path)
                .stream(new ByteArrayInputStream(bytes), size, -1)
                .build());

        log.info("上传附件到minio成功, bucket: {}, path: {}", minioConfig.getBucket(), path);

        // 熟悉设置
        AttachmentDO attachmentDO = new AttachmentDO();
        attachmentDO.setAttachmentType(attachmentType)
                .setAttachmentName(attachmentName)
                .setExtension(extension)
                .setPath(path)
                .setSort(1L)
                .setCreateTime(LocalDateTime.now());
        // 保存上传附件记录
        attachmentMapper.insert(attachmentDO);

        // 属性拷贝
        AttachmentVO attachmentVO = new AttachmentVO();
        BeanUtils.copyProperties(attachmentDO, attachmentVO);
        attachmentVO.setUrl(getUrl(attachmentDO.getPath()));

        return attachmentVO;
    }

    @Transactional
    @Override
    public List<AttachmentVO> setAttachmentDocId(Long docId, AttachmentType attachmentType, String content) {
        // 删除已经关联的
        List<AttachmentVO> attachmentVOList = getAttachmentVOList(attachmentType, docId);
        Set<Long> removeIds = attachmentVOList.stream().map(AttachmentVO::getId).collect(Collectors.toSet());

        // 从富文本中解析附件id
        List<Long> attachmentIds = getAttachmentIdsByContent(docId, attachmentType, content);

        removeIds.removeAll(attachmentIds);

        for (Long removeId : removeIds) {
            AttachmentDO attachmentDO = attachmentMapper.selectById(removeId);
            if (attachmentDO == null) {
                continue;
            }
            attachmentDO.setDocId(null);
            attachmentMapper.updateById(attachmentDO);
        }

        List<AttachmentDO> attachmentDOList = attachmentMapper.selectBatchIds(attachmentIds);

        // 执行数据关联
        for (AttachmentDO attachmentDO : attachmentDOList) {
            attachmentDO.setDocId(docId);
            attachmentDO.setAttachmentType(attachmentType);
            attachmentMapper.updateById(attachmentDO);
        }
        log.info("为附件设置docId成功, docId: {}, attachmentIds: {}", docId, attachmentDOList);
        return copyDOToAttachmentVO(attachmentDOList);
    }

    /**
     * 从富文本中解析附件ID
     * @param docId 文档ID
     * @param attachmentType 附件类型
     * @param content 富文本文本内容
     * @return 附件ID集合
     */
    public List<Long> getAttachmentIdsByContent(Long docId, AttachmentType attachmentType, String content) {
        // TODO 从富文本中解析附件ID
        return new ArrayList<>();
    }

    @Override
    public List<AttachmentVO> getAttachmentVOList(AttachmentType attachmentType, Long docId) {
        LambdaQueryWrapper<AttachmentDO> queryWrapper = Wrappers.lambdaQuery(AttachmentDO.class)
                .eq(AttachmentDO::getAttachmentType, attachmentType)
                .eq(AttachmentDO::getDocId, docId);
        List<AttachmentDO> attachmentDOList = attachmentMapper.selectList(queryWrapper);
        return copyDOToAttachmentVO(attachmentDOList);
    }

    @Override
    public InputStream downloadAttachment(String path) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucket()).object(path).build());
        } catch (Exception e) {
            log.error("附件下载失败", e);
            throw BizCode._4401.exception();
        }
    }

    /**
     * 拷贝属性到AttachmentVO
     * @param attachmentDOList DO集合
     * @return attachmentVOList
     */
    private List<AttachmentVO> copyDOToAttachmentVO(List<AttachmentDO> attachmentDOList) {
        return attachmentDOList.stream().map(attachmentDO -> {
            AttachmentVO attachmentVO = new AttachmentVO();
            BeanUtils.copyProperties(attachmentDO, attachmentVO);
            attachmentVO.setUrl(getUrl(attachmentDO.getPath()));
            return attachmentVO;
        }).collect(Collectors.toList());
    }
}

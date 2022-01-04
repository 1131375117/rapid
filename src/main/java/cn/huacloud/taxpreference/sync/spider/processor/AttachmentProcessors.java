package cn.huacloud.taxpreference.sync.spider.processor;

import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.config.MinioConfig;
import cn.huacloud.taxpreference.config.SpiderDataSyncConfig;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.sync.spider.SpiderDataSyncScheduler;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangkh
 */
@Slf4j
@Component
public class AttachmentProcessors implements CommandLineRunner {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioClient spiderMinioClient;

    @Autowired
    private SpiderDataSyncConfig spiderDataSyncConfig;

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 处理正文（处理步骤参考 HtmlProcessors），替换正文中的附件链接地址，拷贝附件
     * @param document 解析后的content
     * @param spiderPolicyAttachmentDOList
     * @param attachmentType
     * @return
     */
    public Pair<Document, List<AttachmentDO>> processContentAndAttachment(Document document, List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList, AttachmentType attachmentType) {

        // 附件列表为空不处理附件
        if (CollectionUtils.isEmpty(spiderPolicyAttachmentDOList)) {
            return Pair.of(document, new ArrayList<>());
        }

        // 解析出来的pathUrl
        // 同一url在一个页面可以被多次引用
        Map<String, List<Wrapper>> pathUrlMap = Stream.concat(
                getTargetElementStream(document, "a", "href"),
                getTargetElementStream(document, "img", "src"))
                .collect(Collectors.groupingBy(Wrapper::getPathUrl));

        // 附件中pathUrl
        List<AttachmentDO> attachmentDOList = spiderPolicyAttachmentDOList.stream().filter(spiderPolicyAttachmentDO -> {
            String pathUrl = spiderPolicyAttachmentDO.getPathUrl();
            return pathUrlMap.containsKey(pathUrl);
        }).map(spiderPolicyAttachmentDO -> {

            String sourcePath = spiderPolicyAttachmentDO.getPath();
            String targetPath = "spider/" + sourcePath;

            // 链接地址替换
            pathUrlMap.get(spiderPolicyAttachmentDO.getPathUrl())
                    .forEach(wrapper -> wrapper.getElement().attr(wrapper.getAttrKey(), AttachmentService.getUrl(targetPath)));

            // 文件拷贝
            copyFile(sourcePath, targetPath);

            String attachmentName = spiderPolicyAttachmentDO.getAttachmentName();
            AttachmentDO attachmentDO = new AttachmentDO()
                    .setAttachmentType(attachmentType)
                    .setAttachmentName(attachmentName)
                    .setPath(targetPath)
                    .setCreateTime(LocalDateTime.now())
                    .setSort(1L);
            String extension = StringUtils.substringAfterLast(attachmentName, ".");
            attachmentDO.setExtension(extension);
            return attachmentDO;
        }).collect(Collectors.toList());
        // 返回数据
        return Pair.of(document, attachmentDOList);
    }

    /**
     * 爬虫文件拷贝
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    private void copyFile(String sourcePath, String targetPath) {
        String bucket = spiderDataSyncConfig.getMinio().getBucket();
        try {
            GetObjectResponse object = spiderMinioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(sourcePath).build());
            Headers headers = object.headers();
            long contentLength = Long.parseLong(Objects.requireNonNull(headers.get("Content-Length")));
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(targetPath)
                    .stream(object, contentLength, -1)
                    .build());
        } catch (Exception e) {
            log.error("爬虫文件拷贝失败", e);
        }
    }

    /**
     * 解析文档中的指定元素
     * @param document html文档
     * @param label 标签名称
     * @param attrKey 属性名称
     * @return
     */
    private Stream<Wrapper> getTargetElementStream(Document document, String label, String attrKey) {
        String cssQuery = label + "[" + attrKey + "]";
        return document.select(cssQuery).stream()
                .map(element -> new Wrapper().setElement(element)
                        .setAttrKey(attrKey)
                        .setPathUrl(element.attr(attrKey)));
    }

    @Accessors(chain = true)
    @Data
    public static class Wrapper {
        /**
         * a标签、img标签中的路径值
         */
        private String pathUrl;
        /**
         * 属性名称 url、src
         */
        private String attrKey;
        /**
         * html元素
         */
        private Element element;
    }

    @Override
    public void run(String... args) throws Exception {
        // set spiderMinioClient after server started
        SpiderDataSyncScheduler scheduler = SpringUtil.getBean(SpiderDataSyncScheduler.class);
        spiderMinioClient = scheduler.getSpiderMinioClient();
    }
}

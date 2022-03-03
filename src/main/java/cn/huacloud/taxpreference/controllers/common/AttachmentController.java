package cn.huacloud.taxpreference.controllers.common;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.enums.AttachmentType;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.AttachmentService;
import cn.huacloud.taxpreference.services.common.entity.dtos.AttachmentDownloadDTO;
import cn.huacloud.taxpreference.services.common.entity.vos.AttachmentVO;
import cn.hutool.core.net.URLDecoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author wangkh
 */
@Api(tags = "附件管理")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PermissionInfo(name = "上传附件", group = PermissionGroup.POLICIES)
    @SaCheckPermission("attachment_upload")
    @ApiOperation("上传附件")
    @PostMapping("/attachment/upload")
    public ResultVO<AttachmentVO> uploadAttachment(@RequestParam("attachmentType") AttachmentType attachmentType,
                                                   @RequestParam(value = "name", required = false) String name,
                                                   @RequestPart("file") MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
  /*      if (StringUtils.isBlank(name)) {
            name = StringUtils.substringBeforeLast(originalFilename, ".");
        }*/
        String extension = StringUtils.substringAfterLast(originalFilename, ".");
        AttachmentVO attachmentVO = attachmentService.uploadAttachment(attachmentType, name, extension, file.getInputStream(), file.getSize());
        return ResultVO.ok(attachmentVO);
    }


    @ConsumerUserCheckLogin()
    @ApiOperation("前台用户上传")
    @PostMapping("/attachment/upload_consumer")
    public ResultVO<AttachmentVO> uploadAttachmentByConsumer(@RequestParam("attachmentType") AttachmentType attachmentType,
                                                             @RequestParam(value = "name", required = false) String name,
                                                             @RequestPart("file") MultipartFile file) throws Exception {
        if (attachmentType.equals(AttachmentType.CASE_ANALYSIS) || attachmentType.equals(AttachmentType.POLICIES_EXPLAIN) || attachmentType.equals(AttachmentType.FREQUENTLY_ASKED_QUESTION)
        ) {
            throw BizCode._4501.exception();
        }
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.substringAfterLast(originalFilename, ".");
        AttachmentVO attachmentVO = attachmentService.uploadAttachment(attachmentType, name, extension, file.getInputStream(), file.getSize());
        return ResultVO.ok(attachmentVO);
    }

    @ApiOperation("下载附件")
    @GetMapping("/attachment/download/**")
    public ResponseEntity<InputStreamResource> downloadAttachment(HttpServletRequest request) {
        String uri = StringUtils.substringAfter(request.getRequestURI(), "/attachment/download/");
        String path = URLDecoder.decode(uri, StandardCharsets.UTF_8);
        if (!StringUtils.isEmpty(request.getQueryString())) {
            path = path + "?" + request.getQueryString();
        }        // 获取文件流
        AttachmentDownloadDTO attachmentDownloadDTO = attachmentService.downloadAttachmentWithName(path);
        InputStreamResource resource = new InputStreamResource(attachmentDownloadDTO.getInputStream());

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(attachmentDownloadDTO.getAttachmentName(), StandardCharsets.UTF_8)
                .build());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}

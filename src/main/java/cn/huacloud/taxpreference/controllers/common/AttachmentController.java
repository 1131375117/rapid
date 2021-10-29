package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.common.entity.vos.AttachmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangkh
 */
@Api("附件管理")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class AttachmentController {

    @ApiOperation("上传附件")
    @PostMapping("/attachment/upload")
    public ResultVO<AttachmentVO> uploadAttachment(@RequestParam("") String attachmentName,
                                                   @RequestPart("file")MultipartFile file) {
        return null;
    }
}

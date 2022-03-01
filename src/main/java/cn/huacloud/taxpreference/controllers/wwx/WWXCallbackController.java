package cn.huacloud.taxpreference.controllers.wwx;

import cn.huacloud.taxpreference.services.wwx.WWXService;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wangkh
 */
@Slf4j
@Api(tags = "企业微信回调")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class WWXCallbackController {

    private final WWXService wwxService;

    @ApiOperation("验证URL有效性")
    @GetMapping("/wwx/callback/*")
    public ResponseEntity<String> verifyURL(HttpServletRequest request, CallbackQueryDTO queryDTO) throws Exception {
        log.info("验证URL有效性回调，URL：{}", request.getRequestURL());
        String result = wwxService.verifyURL(queryDTO);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation("安装完成回调、注册回调")
    @PostMapping("/wwx/callback/install")
    public ResponseEntity<String> installCallback(CallbackQueryDTO queryDTO, @RequestBody String bodyStr) throws Exception {
        String result = wwxService.installCallback(queryDTO, bodyStr);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation("数据回调")
    @PostMapping("/wwx/callback/data")
    public ResponseEntity<String> dataCallback(CallbackQueryDTO queryDTO, @RequestBody String bodyStr) throws Exception {
        String result = wwxService.dataCallback(queryDTO, bodyStr);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation("指令回调")
    @PostMapping("/wwx/callback/instruct")
    public ResponseEntity<String> instructCallback(CallbackQueryDTO queryDTO, @RequestBody String bodyStr) throws Exception {
        String result = wwxService.instructCallback(queryDTO, bodyStr);
        return ResponseEntity.ok().body(result);
    }
}

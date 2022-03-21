package cn.huacloud.taxpreference.controllers.wework;

import cn.huacloud.taxpreference.services.wework.CallbackService;
import cn.huacloud.taxpreference.services.wework.entity.model.CallbackQuery;
import cn.huacloud.taxpreference.services.wework.support.WeWorkConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangkh
 */
@Slf4j
@Api(tags = "企业微信税务小工具回调")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TaxToolWeWorkCallbackController {

    private final String appName = WeWorkConstants.APP_NAME_TAX_TOOL;

    @Autowired
    private CallbackService callbackService;

    @ApiOperation("验证URL有效性")
    @GetMapping("/wwx/tax-tool/callback/*")
    public ResponseEntity<String> verifyURL(HttpServletRequest request, CallbackQuery query) throws Exception {
        log.info("验证URL有效性回调，URL：{}", request.getRequestURL());
        String result = callbackService.verifyUrl(appName, query);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation("安装完成回调、注册回调")
    @PostMapping("/wwx/tax-tool/callback/install")
    public ResponseEntity<String> installCallback(CallbackQuery query, @RequestBody String postData) throws Exception {
        String result = callbackService.installCallback(appName, query, postData);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation("数据回调")
    @PostMapping("/wwx/tax-tool/callback/data")
    public ResponseEntity<String> dataCallback(CallbackQuery query, @RequestBody String postData) throws Exception {
        String result = callbackService.dataCallback(appName, query, postData);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation("指令回调")
    @PostMapping("/wwx/tax-tool/callback/instruct")
    public ResponseEntity<String> instructCallback(CallbackQuery query, @RequestBody String postData) throws Exception {
        String result = callbackService.instructCallback(appName, query, postData);
        return ResponseEntity.ok().body(result);
    }
}

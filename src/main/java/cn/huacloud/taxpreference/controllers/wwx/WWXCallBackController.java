package cn.huacloud.taxpreference.controllers.wwx;

import cn.huacloud.taxpreference.services.wwx.WWXService;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallBackQueryDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangkh
 */
@Slf4j
@Api(tags = "企业微信回调")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class WWXCallBackController {

    private final WWXService wwxService;

    @GetMapping("/wwx/call-back/*")
    public ResponseEntity<String> callBackGet(CallBackQueryDTO query) throws Exception {
        String msg = wwxService.callBackGet(query);
        return ResponseEntity.ok().body(msg);
    }

    @PostMapping("/wwx/call-back/*")
    public ResponseEntity<String> callBackPost(CallBackQueryDTO query, @RequestBody String bodyStr) throws Exception {
        String msg = wwxService.callBackPost(query, bodyStr);
        return ResponseEntity.ok().body(msg);
    }
}

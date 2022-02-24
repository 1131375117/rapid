package cn.huacloud.taxpreference.openapi.apis;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.openapi.OpenApiService;
import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiAccessKeyDO;
import cn.huacloud.taxpreference.services.openapi.entity.vos.TokenInfoVO;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@ApiSupport(order = 0)
@Slf4j
@Api(tags = "OpenAPI授权")
@RequiredArgsConstructor
@RequestMapping("/open-api/v1")
@RestController
public class AuthApi {

    private final OpenApiService openApiService;

    @ApiOperation("获取OpenAPI授权访问token")
    @PostMapping("/auth/token")
    public ResultVO<TokenInfoVO> getAccessToken(@RequestParam("accessKeyId") String accessKeyId,
                                                @RequestParam("accessKeySecret") String accessKeySecret) {
        ApiAccessKeyDO apiAccessKeyDO = openApiService.queryApiAccessKey(accessKeyId, accessKeySecret);
        // 密钥不正确
        if (apiAccessKeyDO == null) {
            throw BizCode._4700.exception();
        }
        // 密钥被禁用
        if (!apiAccessKeyDO.getEnable()) {
            throw BizCode._4701.exception();
        }
        OpenApiStpUtil.login(apiAccessKeyDO.getAccessKeyId());

        SaTokenInfo tokenInfo = OpenApiStpUtil.getTokenInfo();

        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setToken(tokenInfo.getTokenValue());

        long tokenTimeout = tokenInfo.getTokenTimeout();

        LocalDateTime invalidTime;
        if (tokenTimeout < 0) {
            invalidTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        } else {
            invalidTime = LocalDateTime.now().plusSeconds(tokenTimeout);
        }

        tokenInfoVO.setInvalidTime(invalidTime);
        return ResultVO.ok(tokenInfoVO);
    }

}

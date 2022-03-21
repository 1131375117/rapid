package cn.huacloud.taxpreference.controllers.wework;

import cn.huacloud.taxpreference.common.enums.user.ChannelUserType;
import cn.huacloud.taxpreference.common.utils.ConsumerStpUtil;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.config.WeWorkConfig;
import cn.huacloud.taxpreference.services.user.ChannelUserService;
import cn.huacloud.taxpreference.services.user.ConsumerUserService;
import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import cn.huacloud.taxpreference.services.user.entity.vos.ConsumerLoginUserVO;
import cn.huacloud.taxpreference.services.wework.WeWorkTokenService;
import cn.huacloud.taxpreference.services.wework.entity.dtos.UserInfo3rdDTO;
import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 税小秘企业微信授权登录
 * @author wangkh
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TaxPreferenceWeWorkOAuth2Controller {

    public static final String REDIRECT_STATE = "WWX_AUTH_REDIRECT";

    private static final String REDIRECT_URL_PATTERN = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={}&redirect_uri={}&response_type=code&scope={}&state={}#wechat_redirect";

    private Pattern hostPattern = Pattern.compile("http[s]?://[^/]+");

    private final WeWorkConfig weWorkConfig;

    private final WeWorkTokenService weWorkTokenService;

    private final ChannelUserService channelUserService;

    private final ConsumerUserService consumerUserService;

    @GetMapping("/wwx/oauth2/redirect")
    public void oauth2Redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AppConfig appConfig = weWorkConfig.getTaxPreference();

        Matcher matcher = hostPattern.matcher(request.getRequestURL().toString());
        matcher.find();
        String host = matcher.group();
        String loginUrl = host + "/api/v1/wwx/oauth2/login";

        List<Object> args = new ArrayList<>();
        // appid
        args.add(appConfig.getSuiteId());
        // redirect_uri
        args.add(loginUrl);
        // scope
        args.add("snsapi_userinfo");
        // state
        args.add(REDIRECT_STATE);

        String redirectUrl = MessageFormatter.arrayFormat(REDIRECT_URL_PATTERN, args.toArray()).getMessage();
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/wwx/oauth2/login")
    public void oauth2Login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取用户身份信息
        String code = request.getParameter("code");
        String appName = weWorkConfig.getAppNameByUri(request.getRequestURI());
        UserInfo3rdDTO userInfo3rdDTO = weWorkTokenService.getUserInfo3rdDTO(appName, code);
        String openUserId = userInfo3rdDTO.lookupOpenUserId();
        ChannelUserType channelType = userInfo3rdDTO.lookupChannelUserType();

        // 创建或返回渠道用户
        ConsumerUserDO consumerUserDO = channelUserService.saveOrReturnByOpenUserIdAndChannelType(openUserId, channelType, userInfo3rdDTO.getCorpId());
        // 获取登录视图用户，为了和登录保持代码逻辑一致
        ConsumerLoginUserVO loginUserVO = consumerUserService.getLoginUserVO(consumerUserDO.getUserAccount());
        // 执行登录
        ConsumerStpUtil.login(loginUserVO.getId());
        // 保存用户登录视图到 session
        ConsumerStpUtil.getSession().set(ConsumerUserUtil.CONSUMER_USER, loginUserVO);

        Matcher matcher = hostPattern.matcher(request.getRequestURL().toString());
        matcher.find();
        String host = matcher.group();
        String homeUrl = host + "/";

        response.sendRedirect(homeUrl);
    }
}

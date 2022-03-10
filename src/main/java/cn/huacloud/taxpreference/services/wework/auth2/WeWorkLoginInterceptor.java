package cn.huacloud.taxpreference.services.wework.auth2;

import cn.huacloud.taxpreference.common.enums.user.ChannelUserType;
import cn.huacloud.taxpreference.common.utils.ConsumerStpUtil;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.config.WeWorkConfig;
import cn.huacloud.taxpreference.services.user.ChannelUserService;
import cn.huacloud.taxpreference.services.user.ConsumerUserService;
import cn.huacloud.taxpreference.services.user.entity.dos.ChannelUserDO;
import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import cn.huacloud.taxpreference.services.user.entity.vos.ConsumerLoginUserVO;
import cn.huacloud.taxpreference.services.wework.WeWorkTokenService;
import cn.huacloud.taxpreference.services.wework.entity.dtos.UserInfo3rdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangkh
 */
@Component
public class WeWorkLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private WeWorkTokenService weWorkTokenService;
    @Autowired
    private WeWorkConfig weWorkConfig;
    @Autowired
    private ChannelUserService channelUserService;
    @Autowired
    private ConsumerUserService consumerUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String state = request.getParameter("state");
        if (WeWorkCheckRedirectComponent.REDIRECT_STATE.equals(state)) {
            String code = request.getParameter("code");
            String appName = weWorkConfig.getAppNameByUri(request.getRequestURI());
            UserInfo3rdDTO userInfo3rdDTO = weWorkTokenService.getUserInfo3rdDTO(appName, code);
            String openUserId = userInfo3rdDTO.lookupOpenUserId();
            ChannelUserType channelType = userInfo3rdDTO.lookupChannelUserType();
            // 创建或返回渠道用户
            ConsumerUserDO consumerUserDO = channelUserService.saveOrReturnByOpenUserIdAndChannelType(openUserId, channelType, userInfo3rdDTO.getCorpId());
            // 获取登录视图用户，为了和登录保持代码逻辑一致
            ConsumerLoginUserVO loginUserVO = consumerUserService.getLoginUserVOWithPassword(consumerUserDO.getUserAccount());
            // 执行登录
            ConsumerStpUtil.login(loginUserVO.getId());
            // 保存用户登录视图到 session
            ConsumerStpUtil.getSession().set(ConsumerUserUtil.CONSUMER_USER, loginUserVO);
        }
        return true;
    }
}

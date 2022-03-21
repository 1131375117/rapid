package cn.huacloud.taxpreference.services.openapi.auth;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.user.ChannelUserType;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.user.ChannelUserService;
import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OpenUserId拦截器
 * <p/> 拦截请求头中包含OpenUserId的接口，自动创建渠道用户并设置ConsumerUserId缓存
 * @author wangkh
 */
public class OpenUserIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        OpenApiCheckOpenUserId annotation = handlerMethod.getMethod().getAnnotation(OpenApiCheckOpenUserId.class);
        String openUserId = request.getHeader(OpenApiStpUtil.OPEN_USER_ID_NAME);
        if (annotation == null && openUserId == null) {
            // 没有添加注解，并且请求头里没有OpenUserId直接跳过
            return true;
        }

        if (openUserId == null) {
            // 没有找到请求头直接抛出异常结束
            throw BizCode._47056.exception();
        }

        // 设置OpenUserId
        OpenApiStpUtil.openUserIdThreadLocal.set(openUserId);

        // 获取渠道用户ID，不能调整代码顺序，一定要先设置OpenUserId才能调用
        Long consumerUserId = OpenApiStpUtil.getConsumerUserId();
        if (consumerUserId == null) {
            // 这个类型可以直接修改成OpenAPI
            ChannelUserType channelUserType = ChannelUserType.OPEN_API;
            String loginId = OpenApiStpUtil.getLoginIdAsString();
            // 保存或获取渠道用户
            // 以后可能会有多个OpenAPI渠道，在扩展字段1中保存akId以做区分
            ChannelUserService channelUserService = SpringUtil.getBean(ChannelUserService.class);
            ConsumerUserDO consumerUserDO = channelUserService.saveOrReturnByOpenUserIdAndChannelType(openUserId, channelUserType, loginId);
            OpenApiStpUtil.setConsumerUserId(openUserId, consumerUserDO.getId());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除当前线程的OpenUserId
        OpenApiStpUtil.openUserIdThreadLocal.remove();
    }
}

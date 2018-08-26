package com.whuthm.happychat.controller.util;

import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.service.authentication.AuthenticationService;
import com.whuthm.happychat.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenInterceptor.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //如果验证token失败，并且方法注明了Authorization，返回401错误
        final Token token = method.getAnnotation(Token.class);
        if (token != null && token.check()) {
            LOGGER.warn(request.getRequestURI() + " need to check token");
            //从header中得到token
            String tokenValue = request.getHeader(Constants.HEADER_TOKEN);
            String userId = request.getHeader(Constants.HEADER_USER_ID);
            String clientResource = request.getHeader(Constants.HEADER_CLIENT_RESOURCE);
            //验证token
            if (!StringUtils.isEmpty(tokenValue)
                    && tokenValue.equals(authenticationService.getToken(userId, ClientProtos.ClientResource.valueOf(clientResource)))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }
}

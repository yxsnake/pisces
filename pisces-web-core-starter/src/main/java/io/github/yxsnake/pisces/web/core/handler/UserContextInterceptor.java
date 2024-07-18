package io.github.yxsnake.pisces.web.core.handler;

import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.web.core.constant.Common;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import io.github.yxsnake.pisces.web.core.base.LoginUser;
import io.github.yxsnake.pisces.web.core.context.UserContext;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLDecoder;

@Slf4j
public class UserContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userContext = request.getHeader(Common.USER_CONTEXT);
        if(StrUtil.isNotBlank(userContext)){
            LoginUser loginUser = JsonUtils.jsonCovertToObject(URLDecoder.decode(userContext,"UTF-8"), LoginUser.class);
            UserContext.set(loginUser);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.remove();
    }


}

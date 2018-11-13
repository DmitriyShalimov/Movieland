package com.shalimov.movieland.web.interceptor;

import com.shalimov.movieland.entity.User;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

public class LoggingInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) {
            MDC.put("loggedUser", user.getEmail());
        } else {
            MDC.put("loggedUser", "guest");
        }
        MDC.put("requestId", UUID.randomUUID().toString());
        return true;
    }
}

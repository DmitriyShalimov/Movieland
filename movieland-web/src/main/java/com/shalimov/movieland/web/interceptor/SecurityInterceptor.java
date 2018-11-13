package com.shalimov.movieland.web.interceptor;

import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.entity.UserType;
import com.shalimov.movieland.service.SecurityService;
import com.shalimov.movieland.web.annotation.ProtectedBy;
import com.shalimov.movieland.web.entity.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


public class SecurityInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private SecurityService securityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ProtectedBy protectedByAnnotation = handlerMethod.getMethod().getAnnotation(ProtectedBy.class);
        if (protectedByAnnotation == null) {
            return true;
        }
        String token = request.getHeader("loggedUser");
        if (token == null) {
            return false;
        } else {
            User user;
            Optional<User> optionalUser = securityService.getUser(token);
            if (optionalUser.isPresent()) {
                user=optionalUser.get();
                if(user.getUserType().equals(UserType.ADMIN)){
                    UserHandler.setUser(user);
                    return true;
                }else{
                    UserType expectedType = protectedByAnnotation.value();
                    if(expectedType.equals(UserType.USER)){
                        UserHandler.setUser(user);
                        return true;
                    }else return false;
                }
            } else {
                return false;
            }
        }
    }
}

package com.leyou.cart.interceptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.utils.CookieUtils2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cq
 * @create 2018-08-05 21:42
 * @copy alibaba
 */

public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    public UserInterceptor(JwtProperties jwtProperties) {
         this.jwtProperties =jwtProperties;
    }

    private static final  ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取cookie
        String tooken = CookieUtils2.getCookieValue(request, jwtProperties.getCookieName());

        try {
            //解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(tooken, jwtProperties.getPublicKey());
           //存储用户信息
            tl.set(userInfo);
            return  true;
        } catch (Exception e) {

            response.setStatus(401);

            return  false;
        }
     
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
         //最后清空线程域得数据
        tl.remove();
    }

    public static UserInfo getUser(){

        return tl.get();
    }
}
